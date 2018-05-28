package com.savik.service.bookmaker;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BookmakerEventListener {

    @EventListener
    public void handleBookmakerResponse(final BookmakerMatchResponseEvent event) {
        log.debug("event received: event");
    }
}
