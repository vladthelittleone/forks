package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@Log4j2
public abstract class BookmakerService {

    @Autowired
    BookmakerMatchService bookmakerMatchService;

    @Autowired
    BookmakerEventPublisher bookmakerEventPublisher;

    protected abstract BookmakerType getBookmakerType();

    protected abstract Optional<BookmakerMatchResponse> handle(BookmakerMatch match);

    public CompletableFuture<Optional<BookmakerMatchResponse>> handle(Match match) {
        return handle(match, true);
    }
    
    public CompletableFuture<Optional<BookmakerMatchResponse>> handleWithoutPublishing(Match match) {
        return handle(match, false);
    }

    private CompletableFuture<Optional<BookmakerMatchResponse>> handle(Match match, boolean publish) {
        Optional<BookmakerMatch> bookmakerMatchOptional = bookmakerMatchService.createFromMatch(match, getBookmakerType());
        if(!bookmakerMatchOptional.isPresent()) {
            log.debug(String.format("Match wasn't parsed. id: %s, %s-%s",
                    match.getFlashscoreId(), match.getHomeTeam().getName(), match.getAwayTeam().getName()));
            bookmakerEventPublisher.publishMatchInfoNotFoundForBookmaker(match, getBookmakerType());
            return CompletableFuture.completedFuture(null);
        }
        BookmakerMatch bookmakerMatch = bookmakerMatchOptional.get();
        Optional<BookmakerMatchResponse> info = handle(bookmakerMatch);
        if (info.isPresent()) {
            log.trace("Match was parsed:" + info.get());
            if(publish) {
                bookmakerEventPublisher.publishMatchResponse(info.get(), match);
            }
        } else {
            log.info(String.format("Match wasn't found. Id: %s", match));
            if(publish) {
                bookmakerEventPublisher.publishMatchNotFound(bookmakerMatch);
            }
        }
        return CompletableFuture.completedFuture(info);
    }

}
