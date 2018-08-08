package com.savik.service.bookmaker.sbobet;

import com.savik.domain.BookmakerType;
import com.savik.domain.SportType;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.CoeffType;
import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.COMMON;
import static com.savik.service.bookmaker.CoeffType.FIRST_HALF;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static com.savik.service.bookmaker.CoeffType.OVER;
import static com.savik.service.bookmaker.CoeffType.TOTAL;
import static com.savik.service.bookmaker.CoeffType.UNDER;
import static com.savik.service.bookmaker.sbobet.SbobetParser.AWAY_MATCH_TOTAL;
import static com.savik.service.bookmaker.sbobet.SbobetParser.FIRST_HALF_AWAY_MATCH_TOTAL;
import static com.savik.service.bookmaker.sbobet.SbobetParser.FIRST_HALF_HANDICAP;
import static com.savik.service.bookmaker.sbobet.SbobetParser.FIRST_HALF_HOME_MATCH_TOTAL;
import static com.savik.service.bookmaker.sbobet.SbobetParser.FIRST_HALF__HOME_OR_AWAY;
import static com.savik.service.bookmaker.sbobet.SbobetParser.FIRST_HALF__TOTAL;
import static com.savik.service.bookmaker.sbobet.SbobetParser.GUEST_COEFF_INDEX;
import static com.savik.service.bookmaker.sbobet.SbobetParser.HOME_COEFF_INDEX;
import static com.savik.service.bookmaker.sbobet.SbobetParser.HOME_MATCH_TOTAL;
import static com.savik.service.bookmaker.sbobet.SbobetParser.MATCH_HANDICAP;
import static com.savik.service.bookmaker.sbobet.SbobetParser.MATCH_HOME_OR_AWAY;
import static com.savik.service.bookmaker.sbobet.SbobetParser.MATCH_TOTAL;

@Component
public class SbobetParser {
    public static final int MATCHES_ARRAYS_INDEX = 2;
    public static final int LEAGUE_ID_INDEX = 1;
    public static final int MATCH_INFO_INDEX = 2;
    public static final int MATCH_COEFFS_INDEX = 4;
    public static final int MATCH_ID_INDEX = 0;
    public static final int HOME_NAME_INDEX = 1;
    public static final int GUEST_NAME_INDEX = 2;
    public static final int HOME_COEFF_INDEX = 0;
    public static final int GUEST_COEFF_INDEX = 1;
    public static final int MATCHES_IN_CONTAINER_INDEX = 1;
    public static final int LIVE_MATCHES_INDEX = 0;
    public static final int PREMATCH_MATCHES_WHILE_LIVE_EXISTS_INDEX = 1;
    public static final int PREMATCH_MATCHES_WHILE_LIVE_NOT_EXISTS_INDEX = 0;


    public static final int MATCH_HANDICAP = 1;
    public static final int MATCH_TOTAL = 3;
    public static final int MATCH_HOME_OR_AWAY = 5;
    public static final int FIRST_HALF__HOME_OR_AWAY = 8;
    public static final int FIRST_HALF_HANDICAP = 7;
    public static final int FIRST_HALF__TOTAL = 9;
    public static final int HOME_MATCH_TOTAL = 545;
    public static final int FIRST_HALF_HOME_MATCH_TOTAL = 546;
    public static final int FIRST_HALF_AWAY_MATCH_TOTAL = 548;
    public static final int AWAY_MATCH_TOTAL = 547;

    private static final List<BetParser> PARSERS = new ArrayList<BetParser>() {
        {
            add(new MatchHandicap());
            add(new FirstHalfHandicap());
            add(new MatchTotal());
            add(new FirstHalfTotal());
            add(new HomeMatchTotal());
            add(new FirstHalfHomeMatchTotal());
            add(new FirstHalfAwayMatchTotal());
            add(new AwayMatchTotal());
            add(new MatchHomeOrAway());
            add(new FirstHalfHomeOrAway());
        }
    };


    @Autowired
    SbobetDownloader downloader;

    public List<BookmakerMatchResponse> getMatchesBySport(SportType sportType, int daysFromToday) {
        Document download = downloader.download(sportType, daysFromToday);
        List<BookmakerMatchResponse> matches = getMatches(download);
        return matches;
    }

    private List<BookmakerMatchResponse> getMatches(Document document) {
        JSONArray arrayContainer = extractArrayFromHtml(document);
        List<BookmakerMatchResponse> bookmakerMatchResponses = new ArrayList<>();
        JSONArray liveAndPrematchArrays = arrayContainer.getJSONArray(MATCHES_ARRAYS_INDEX);
        // live and prematch
        if (liveAndPrematchArrays.length() == 2) {
            bookmakerMatchResponses.addAll(
                    getMatchesFromArray(
                            liveAndPrematchArrays.getJSONArray(LIVE_MATCHES_INDEX)
                                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX)
                    )
            );
            bookmakerMatchResponses.addAll(
                    getMatchesFromArray(
                            liveAndPrematchArrays.getJSONArray(PREMATCH_MATCHES_WHILE_LIVE_EXISTS_INDEX)
                                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX)
                    )
            );
        } else if (liveAndPrematchArrays.length() == 1) {         // only prematch
            bookmakerMatchResponses.addAll(
                    getMatchesFromArray(
                            liveAndPrematchArrays.getJSONArray(PREMATCH_MATCHES_WHILE_LIVE_NOT_EXISTS_INDEX)
                                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX)
                    )
            );
        }
        return bookmakerMatchResponses;

    }

    public Optional<BookmakerMatchResponse> downloadAndParseSingleMatch(String bookmakerMatchId, BookmakerMatch bookmakerMatch) {
        Document document = downloader.download(bookmakerMatchId, bookmakerMatch);
        JSONArray arrayContainer = extractArrayFromHtml(document);
        JSONArray prematchArrays = arrayContainer.getJSONArray(MATCHES_ARRAYS_INDEX);
        List<BookmakerMatchResponse> bookmakerMatchResponses = getMatchesFromArray(prematchArrays.getJSONArray(0).getJSONArray(MATCHES_IN_CONTAINER_INDEX));
        if (bookmakerMatchResponses.isEmpty() || bookmakerMatchResponses.size() != 1) {
            throw new IllegalStateException("check");
        }
        return Optional.of(bookmakerMatchResponses.get(0));
    }

    private JSONArray extractArrayFromHtml(Document download) {
        Element script = download.getElementsByTag("script").last();
        String scriptText = script.childNodes().get(0).toString();
        String jsArray = scriptText.substring(scriptText.indexOf("onUpdate('od',") + 14, scriptText.indexOf("]);") + 1);
        return new JSONArray(jsArray);
    }

    private List<BookmakerMatchResponse> getMatchesFromArray(JSONArray matchesArray) {
        List<BookmakerMatchResponse> bookmakerMatchResponses = new ArrayList<>();
        for (int i = 0; i < matchesArray.length(); i++) {
            JSONArray matchArray = matchesArray.getJSONArray(i);
            int sbobetLeagueId = matchArray.getInt(LEAGUE_ID_INDEX);
            JSONArray matchInfoArray = matchArray.getJSONArray(MATCH_INFO_INDEX);

            int matchId = matchInfoArray.getInt(MATCH_ID_INDEX);
            String homeTeamName = matchInfoArray.getString(HOME_NAME_INDEX);
            String guestTeamName = matchInfoArray.getString(GUEST_NAME_INDEX);

            JSONArray matchCoeffsArray = matchArray.getJSONArray(MATCH_COEFFS_INDEX);

            Set<BookmakerCoeff> bookmakerCoeffs = new HashSet<>();
            // j = 1, because, first element is info array
            for (int j = 1; j < matchCoeffsArray.length(); j++) {
                JSONArray coeffArrayContainer = matchCoeffsArray.getJSONArray(j);
                JSONArray handicapValueArray = coeffArrayContainer.getJSONArray(1);
                int betType = handicapValueArray.getInt(0);


                for (BetParser parser : PARSERS) {
                    if (parser.couldApply(betType)) {
                        Set<BookmakerCoeff> coeffs = new HashSet<>(parser.apply(coeffArrayContainer));
                        bookmakerCoeffs.addAll(coeffs);
                    }
                }
            }
            if (!bookmakerCoeffs.isEmpty()) {
                BookmakerMatchResponse bookmakerMatchResponse = BookmakerMatchResponse.builder()
                        .bookmakerType(BookmakerType.SBOBET)
                        .bookmakerCoeffs(new ArrayList<>(bookmakerCoeffs))
                        .bookmakerAwayTeamName(guestTeamName)
                        .bookmakerHomeTeamName(homeTeamName)
                        .bookmakerLeagueId(String.valueOf(sbobetLeagueId))
                        .bookmakerMatchId(String.valueOf(matchId))
                        .build();

                //log.debug("Match was parsed: " + bookmakerMatchResponse);
                bookmakerMatchResponses.add(bookmakerMatchResponse);
            }
        }
        return bookmakerMatchResponses;
    }
}

interface BetParser {
    boolean couldApply(Integer betType);

    List<BookmakerCoeff> apply(JSONArray betArrayContainer);
}

class CommonHandicap implements BetParser {

    @Override
    public boolean couldApply(Integer betType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        throw new UnsupportedOperationException();
    }

    public List<BookmakerCoeff> apply(JSONArray betArrayContainer, CoeffType period) {
        JSONArray handicapValueArray = betArrayContainer.getJSONArray(1);
        double handicapValue = handicapValueArray.getDouble(5);
        JSONArray coeffValueArray = betArrayContainer.getJSONArray(2);
        double homeCoeffValue = coeffValueArray.getDouble(HOME_COEFF_INDEX);
        double guestCoeffValue = coeffValueArray.getDouble(GUEST_COEFF_INDEX);
        BookmakerCoeff homeCoeff = BookmakerCoeff.of(-handicapValue, homeCoeffValue, period, HOME, HANDICAP);
        BookmakerCoeff guestCoeff = BookmakerCoeff.of(handicapValue, guestCoeffValue, period, AWAY, HANDICAP);
        List<BookmakerCoeff> bookmakerCoeffs = new ArrayList<>();
        bookmakerCoeffs.add(homeCoeff);
        bookmakerCoeffs.add(guestCoeff);
        return bookmakerCoeffs;
    }
}

class CommonTotal implements BetParser {

    @Override
    public boolean couldApply(Integer betType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        throw new UnsupportedOperationException();
    }

    public List<BookmakerCoeff> apply(JSONArray betArrayContainer, CoeffType period, CoeffType type) {
        JSONArray handicapValueArray = betArrayContainer.getJSONArray(1);
        double totalValue = handicapValueArray.getDouble(5);
        JSONArray coeffValueArray = betArrayContainer.getJSONArray(2);
        double homeCoeffValue = coeffValueArray.getDouble(HOME_COEFF_INDEX);
        double guestCoeffValue = coeffValueArray.getDouble(GUEST_COEFF_INDEX);
        BookmakerCoeff overCoeff = BookmakerCoeff.of(totalValue, homeCoeffValue, period, type, TOTAL, OVER);
        BookmakerCoeff underCoeff = BookmakerCoeff.of(totalValue, guestCoeffValue, period, type, TOTAL, UNDER);
        List<BookmakerCoeff> bookmakerCoeffs = new ArrayList<>();
        bookmakerCoeffs.add(overCoeff);
        bookmakerCoeffs.add(underCoeff);
        return bookmakerCoeffs;
    }
}

class HomeOrAway implements BetParser {

    @Override
    public boolean couldApply(Integer betType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        throw new UnsupportedOperationException();
    }

    public List<BookmakerCoeff> apply(JSONArray betArrayContainer, CoeffType period) {
        JSONArray coeffValueArray = betArrayContainer.getJSONArray(2);
        double homeCoeffValue = coeffValueArray.getDouble(0);
        double guestCoeffValue = coeffValueArray.getDouble(2);
        BookmakerCoeff overCoeff = BookmakerCoeff.of(-0.5, homeCoeffValue, period, HOME, HANDICAP);
        BookmakerCoeff underCoeff = BookmakerCoeff.of(-0.5, guestCoeffValue, period, AWAY, HANDICAP);
        List<BookmakerCoeff> bookmakerCoeffs = new ArrayList<>();
        bookmakerCoeffs.add(overCoeff);
        bookmakerCoeffs.add(underCoeff);
        return bookmakerCoeffs;
    }
}

class MatchHomeOrAway extends HomeOrAway {
    
    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, MATCH);
    }
    
    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(MATCH_HOME_OR_AWAY);
    }
}

class FirstHalfHomeOrAway extends HomeOrAway {
    
    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, FIRST_HALF);
    }
    
    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(FIRST_HALF__HOME_OR_AWAY);
    }
}

class MatchHandicap extends CommonHandicap {

    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(MATCH_HANDICAP);
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, MATCH);
    }
}

class FirstHalfHandicap extends CommonHandicap {

    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(FIRST_HALF_HANDICAP);
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, FIRST_HALF);
    }
}

class MatchTotal extends CommonTotal {

    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(MATCH_TOTAL);
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, MATCH, COMMON);
    }
}

class FirstHalfTotal extends CommonTotal {

    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(FIRST_HALF__TOTAL);
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, FIRST_HALF, COMMON);
    }
}

class HomeMatchTotal extends CommonTotal {

    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(HOME_MATCH_TOTAL);
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, MATCH, HOME);
    }
}

class FirstHalfHomeMatchTotal extends CommonTotal {

    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(FIRST_HALF_HOME_MATCH_TOTAL);
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, FIRST_HALF, HOME);
    }
}

class FirstHalfAwayMatchTotal extends CommonTotal {

    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(FIRST_HALF_AWAY_MATCH_TOTAL);
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, FIRST_HALF, AWAY);
    }
}

class AwayMatchTotal extends CommonTotal {

    @Override
    public boolean couldApply(Integer betType) {
        return betType.equals(AWAY_MATCH_TOTAL);
    }

    @Override
    public List<BookmakerCoeff> apply(JSONArray betArrayContainer) {
        return apply(betArrayContainer, MATCH, AWAY);
    }
}

