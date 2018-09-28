package com.savik.service.bookmaker.matchbook;

import com.savik.domain.BookmakerType;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class MatchbookParser {
    public List<BookmakerMatchResponse> parse(List<Event> events) {
        final ArrayList<BookmakerMatchResponse> bookmakerMatchResponses = new ArrayList<>();
        for (Event event : events) {
            final BookmakerMatchResponse response = BookmakerMatchResponse.builder()
                    .bookmakerAwayTeamName(event.getBookmakerAwayTeamName())
                    .bookmakerHomeTeamName(event.getBookmakerHomeTeamName())
                    .bookmakerLeagueId(String.valueOf(event.getBookmakerLeagueId()))
                    .bookmakerType(BookmakerType.MATCHBOOK)
                    .bookmakerMatchId(String.valueOf(event.getId()))
                    .bookmakerCoeffs(parseCoeffs(event.getMarkets()))
                    .build();
            bookmakerMatchResponses.add(response);
        }
        return bookmakerMatchResponses;
    }
    
    private List<BookmakerCoeff> parseCoeffs(List<Market> markets) {
        return new ArrayList<>();
    }
}
