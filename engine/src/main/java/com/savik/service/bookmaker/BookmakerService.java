package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Log4j2
public abstract class BookmakerService {

    @Autowired
    BookmakerMatchService bookmakerMatchService;

    @Autowired
    BookmakerEventPublisher bookmakerEventPublisher;

    protected abstract BookmakerType getBookmakerType();

    protected abstract Optional<BookmakerMatchResponse> handle(BookmakerMatch match);

    public void handle(Match match) {
        BookmakerMatch bookmakerMatch = bookmakerMatchService.createFromMatch(match, getBookmakerType());
        Optional<BookmakerMatchResponse> info = handle(bookmakerMatch);
        if (info.isPresent()) {
            log.debug("Match was parsed:" + info.get());
            bookmakerEventPublisher.publishMatchResponse(info.get(), match);
        } else {
            log.info("Match wasn't found:" + match);
            bookmakerEventPublisher.publishMatchNotFound(bookmakerMatch);
        }
    }

}
