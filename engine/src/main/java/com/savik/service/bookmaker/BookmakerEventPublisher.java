package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.events.BookmakerMatchInfoNotFoundEvent;
import com.savik.events.BookmakerMatchNotFoundEvent;
import com.savik.events.BookmakerMatchResponseEvent;
import com.savik.events.ForkFoundEvent;
import com.savik.model.BookmakerMatchWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class BookmakerEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishMatchResponse(final BookmakerMatchResponse bookmakerMatchResponse, final BookmakerMatchWrapper match) {
        BookmakerMatchResponseEvent matchResponseEvent = new BookmakerMatchResponseEvent(bookmakerMatchResponse, match);
        applicationEventPublisher.publishEvent(matchResponseEvent);
    }

    public void publishMatchNotFound(final BookmakerMatchWrapper bookmakerMatchWrapper) {
        BookmakerMatchNotFoundEvent matchNotFoundEvent = new BookmakerMatchNotFoundEvent(bookmakerMatchWrapper);
        applicationEventPublisher.publishEvent(matchNotFoundEvent);
    }

    public void publishMatchInfoNotFoundForBookmaker(Match match, BookmakerType bookmakerType) {
        BookmakerMatchInfoNotFoundEvent bookmakerMatchInfoNotFoundEvent = new BookmakerMatchInfoNotFoundEvent(match, bookmakerType);
        applicationEventPublisher.publishEvent(bookmakerMatchInfoNotFoundEvent);
    }
    
    public void publishForkFoundEvent(ForkFoundEvent forkFoundEvent) {
        applicationEventPublisher.publishEvent(forkFoundEvent);
    }
}
