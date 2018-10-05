package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.model.BookmakerMatchWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public abstract class BookmakerService {

    @Autowired
    BookmakerMatchService bookmakerMatchService;

    @Autowired
    BookmakerEventPublisher bookmakerEventPublisher;
    
    Map<BookmakerType, Set<String>> cache = new HashMap<>();

    protected abstract BookmakerType getBookmakerType();

    protected abstract Optional<BookmakerMatchResponse> handle(BookmakerMatchWrapper match);

    public CompletableFuture<Optional<BookmakerMatchResponse>> handle(Match match) {
        return handle(match, true);
    }
    
    public CompletableFuture<Optional<BookmakerMatchResponse>> handleWithoutPublishing(Match match) {
        return handle(match, false);
    }

    private CompletableFuture<Optional<BookmakerMatchResponse>> handle(Match match, boolean publish) {
        Optional<BookmakerMatchWrapper> bookmakerMatchOptional = bookmakerMatchService.createFromMatch(match, getBookmakerType());
        if(!bookmakerMatchOptional.isPresent()) {
            log.debug(String.format("Match bookmaker matching wasn't found. %s, id: %s. %s-%s", getBookmakerType(),
                    match.getFlashscoreId(), match.getHomeTeam().getName(), match.getAwayTeam().getName()));
            bookmakerEventPublisher.publishMatchInfoNotFoundForBookmaker(match, getBookmakerType());
            return CompletableFuture.completedFuture(null);
        }
        BookmakerMatchWrapper bookmakerMatchWrapper = bookmakerMatchOptional.get();
        Optional<BookmakerMatchResponse> info = handle(bookmakerMatchWrapper);
        if (info.isPresent()) {
            log.trace("Match was parsed:" + info.get());
            if(publish) {
                bookmakerEventPublisher.publishMatchResponse(info.get(), bookmakerMatchWrapper);
            }
        } else {
            cache.putIfAbsent(getBookmakerType(), ConcurrentHashMap.newKeySet());
            if(!cache.get(getBookmakerType()).contains(match.getFlashscoreId())) {
                log.info(String.format("Match wasn't found in line: %s, %s, %s", getBookmakerType(),
                        bookmakerMatchWrapper.getDefaultLogString(), bookmakerMatchWrapper.getBookmakerLeague().getName()));
                cache.get(getBookmakerType()).add(match.getFlashscoreId());
            }
            
            if(publish) {
                bookmakerEventPublisher.publishMatchNotFound(bookmakerMatchWrapper);
            }
        }
        return CompletableFuture.completedFuture(info);
    }

}
