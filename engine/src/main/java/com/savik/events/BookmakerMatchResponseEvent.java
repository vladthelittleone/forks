package com.savik.events;

import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerMatchWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmakerMatchResponseEvent {

    BookmakerMatchResponse bookmakerMatchResponse;

    BookmakerMatchWrapper match;

    @Override
    public String toString() {
        return "BMResEvent{" +
                "match=" + match.getMatch().getFlashscoreId() + " (" + match.getHomeTeam().getName() + " - " + match.getAwayTeam() +
                '}';
    }
}
