package com.savik.service.bookmaker.matchbook;

import com.savik.domain.BookmakerType;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.CoeffType;
import com.savik.utils.BookmakerUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.savik.service.bookmaker.CoeffType.*;

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
                    .bookmakerCoeffs(parseCoeffs(event))
                    .build();
            bookmakerMatchResponses.add(response);
        }
        return bookmakerMatchResponses;
    }

    private List<BookmakerCoeff> parseCoeffs(Event event) {
        List<BookmakerCoeff> coeffs = new ArrayList<>();
        List<Market> markets = event.getMarkets();
        List<Market> handicaps = markets.stream().filter(m -> m.getMarketType() == MarketType.HANDICAP).collect(Collectors.toList());
        for (Market market : handicaps) {

            List<Runner> runners = market.getRunners();
            for (Runner runner : runners) {
                Double typeValue = runner.getHandicap() != null ? market.getHandicap() :
                        BookmakerUtils.parseSpecialHandicapString(market.getAsianHandicap(), "/");
                CoeffType side = getSideType(event, runner.getName());
                List<Price> prices = runner.getPrices();
                for (Price price : prices) {
                    if(price.getSide() == Side.BACK) {
                        coeffs.add(BookmakerCoeff.of(typeValue, price.getDecimalOdds(), MATCH, side, HANDICAP));
                    } else {
                        coeffs.add(BookmakerCoeff.of(typeValue, BookmakerUtils.convertLayCoeff(price.getDecimalOdds()), MATCH, side, HANDICAP).lay());
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private CoeffType getSideType(Event event, String runnerName) {
        String[] nameAndHandicap = runnerName.split("\\s+");
        String name = nameAndHandicap[0];
        if (name.equalsIgnoreCase(event.getBookmakerHomeTeamName())) {
            return CoeffType.HOME;
        } else if (name.equalsIgnoreCase(event.getBookmakerAwayTeamName())) {
            return CoeffType.AWAY;
        }
        throw new IllegalArgumentException(String.format("name is incorrect: %s, %s - %s", runnerName,
                event.getBookmakerHomeTeamName(), event.getBookmakerAwayTeamName()));
    }
}
