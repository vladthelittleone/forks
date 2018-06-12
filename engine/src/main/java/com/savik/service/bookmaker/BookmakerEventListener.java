package com.savik.service.bookmaker;

import com.savik.events.BookmakerMatchInfoNotFoundEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BookmakerEventListener {


    @Autowired
    BookmakerFlashscoreConnectionService bookmakerFlashscoreConnectionService;

    @EventListener
    public void handle(final BookmakerMatchInfoNotFoundEvent event) {
        bookmakerFlashscoreConnectionService.printSuggestion(event.getMatch(), event.getBookmakerType());
    }

}
