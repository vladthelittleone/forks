package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BookmakerService {

    @Autowired
    BookmakerMatchService bookmakerMatchService;

    protected abstract BookmakerType getBookmakerType();

    protected abstract void handle(BookmakerMatch match);

    public void handle(Match match) {
        BookmakerMatch bookmakerMatch = bookmakerMatchService.createFromMatch(match, getBookmakerType());
        handle(bookmakerMatch);
    }

}
