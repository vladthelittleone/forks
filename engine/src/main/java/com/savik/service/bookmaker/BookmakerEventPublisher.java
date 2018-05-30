package com.savik.service.bookmaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class BookmakerEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishMatchResponse(final BookmakerMatchResponse bookmakerMatchResponse) {
        BookmakerMatchResponseEvent matchResponseEvent = new BookmakerMatchResponseEvent(bookmakerMatchResponse);
        applicationEventPublisher.publishEvent(matchResponseEvent);
    }

    public void publishMatchNotFound(final BookmakerMatch bookmakerMatch) {
        BookmakerMatchNotFoundEvent matchNotFoundEvent = new BookmakerMatchNotFoundEvent(bookmakerMatch);
        applicationEventPublisher.publishEvent(matchNotFoundEvent);
    }
}
