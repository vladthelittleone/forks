package com.savik.service.bookmaker;

import com.savik.domain.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class BookmakerEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishMatchResponse(final BookmakerMatchResponse bookmakerMatchResponse, final Match match) {
        BookmakerMatchResponseEvent matchResponseEvent = new BookmakerMatchResponseEvent(bookmakerMatchResponse, match);
        applicationEventPublisher.publishEvent(matchResponseEvent);
    }

    public void publishMatchNotFound(final BookmakerMatch bookmakerMatch) {
        BookmakerMatchNotFoundEvent matchNotFoundEvent = new BookmakerMatchNotFoundEvent(bookmakerMatch);
        applicationEventPublisher.publishEvent(matchNotFoundEvent);
    }
}
