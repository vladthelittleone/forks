package com.savik.events;

import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerMatchWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmakerMatchResponseEvent {

    BookmakerMatchResponse bookmakerMatchResponse;

    BookmakerMatchWrapper wrapper;

    @Override
    public String toString() {
        return "BMResEvent{" +
                "wrapper=" + wrapper.getMatch().getFlashscoreId() + " (" + wrapper.getHomeTeam().getName() + " - " + wrapper.getAwayTeam() +
                '}';
    }
}
