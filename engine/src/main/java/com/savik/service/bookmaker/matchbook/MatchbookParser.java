package com.savik.service.bookmaker.matchbook;

import com.savik.domain.BookmakerType;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.CoeffType;
import com.savik.utils.BookmakerUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.savik.service.bookmaker.CoeffType.COMMON;
import static com.savik.service.bookmaker.CoeffType.FIRST_HALF;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static com.savik.service.bookmaker.CoeffType.OVER;
import static com.savik.service.bookmaker.CoeffType.TOTAL;
import static com.savik.service.bookmaker.CoeffType.UNDER;
import static com.savik.service.bookmaker.matchbook.MarketType.HANDICAP_ALSO;

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
        coeffs.addAll(parseHandicaps(event, markets));
        coeffs.addAll(parseTotals(MarketType.TOTAL, MATCH, markets));
        coeffs.addAll(parseTotals(MarketType.FIRST_HALF_TOTAL, FIRST_HALF, markets));
        return coeffs;
    }

    private List<BookmakerCoeff> parseHandicaps(Event event, List<Market> markets) {
        List<BookmakerCoeff> coeffs = new ArrayList<>();
        List<Market> handicaps = markets.stream().filter(m -> m.getMarketType() == MarketType.HANDICAP || m.getMarketType() == HANDICAP_ALSO).collect(Collectors.toList());
        for (Market market : handicaps) {
            List<Runner> runners = market.getRunners();
            for (Runner runner : runners) {
                Double typeValue = runner.getHandicap() != null ? runner.getHandicap() :
                        BookmakerUtils.parseMatchbookSpecialHandicapString(runner.getAsianHandicap(), "/");
                CoeffType side = getSideType(event, runner.getName());
                List<Price> prices = runner.getPrices();
                for (Price price : prices) {
                    if (price.getSide() == Side.BACK) {
                        coeffs.add(BookmakerCoeff.of(typeValue, price.getDecimalOdds(), MATCH, side, HANDICAP));
                    } else {
                        coeffs.add(BookmakerCoeff.of(typeValue, BookmakerUtils.convertLayCoeff(price.getDecimalOdds()), MATCH, side, HANDICAP).lay());
                    }
                }
            }
        }
        return coeffs;
    }

    private List<BookmakerCoeff> parseTotals(MarketType kindOftotal, CoeffType matchPart, List<Market> markets) {
        List<BookmakerCoeff> coeffs = new ArrayList<>();
        List<Market> totals = markets.stream().filter(m -> m.getMarketType() == kindOftotal).collect(Collectors.toList());
        for (Market total : totals) {
            List<Runner> runners = total.getRunners();
            for (Runner runner : runners) {
                Double typeValue = runner.getHandicap() != null ? runner.getHandicap() :
                        BookmakerUtils.parseMatchbookSpecialHandicapString(runner.getAsianHandicap(), "/");
                CoeffType side = getTotalSideType(runner.getName());
                List<Price> prices = runner.getPrices();
                for (Price price : prices) {
                    if (price.getSide() == Side.BACK) {
                        coeffs.add(BookmakerCoeff.of(typeValue, price.getDecimalOdds(), matchPart, COMMON, TOTAL, side));
                    } else {
                        coeffs.add(BookmakerCoeff.of(typeValue, BookmakerUtils.convertLayCoeff(price.getDecimalOdds()), matchPart, COMMON, TOTAL, side).lay());
                    }
                }
            }
        }
        return coeffs;
    }

    private CoeffType getSideType(Event event, String runnerName) {
        String name = getName(runnerName);
        if (name.equalsIgnoreCase(event.getBookmakerHomeTeamName())) {
            return CoeffType.HOME;
        } else if (name.equalsIgnoreCase(event.getBookmakerAwayTeamName())) {
            return CoeffType.AWAY;
        }
        throw new IllegalArgumentException(String.format("name is incorrect: %s, %s - %s", runnerName,
                event.getBookmakerHomeTeamName(), event.getBookmakerAwayTeamName()));
    }

    private CoeffType getTotalSideType(String runnerName) {
        String name = getName(runnerName);
        if (name.equalsIgnoreCase(OVER.toString())) {
            return CoeffType.OVER;
        } else if (name.equalsIgnoreCase(UNDER.toString())) {
            return CoeffType.UNDER;
        }
        throw new IllegalArgumentException(String.format("name is incorrect: %s", runnerName));
    }

    private String getName(String runnerName) {
        List<String> nameAndHandicap = Arrays.asList(runnerName.split("\\s+"));
        return nameAndHandicap.subList(0, nameAndHandicap.size() - 1).stream().reduce((s, s2) -> s + " " + s2).get();
    }
}
