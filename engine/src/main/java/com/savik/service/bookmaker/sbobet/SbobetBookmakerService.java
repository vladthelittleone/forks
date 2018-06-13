package com.savik.service.bookmaker.sbobet;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.MATCH;

@Service
@Log4j2
public class SbobetBookmakerService extends BookmakerService {
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

    @Autowired
    SbobetDownloader downloader;

    @Autowired
    SbobetCache cache;

    @Override
    protected BookmakerType getBookmakerType() {
        return BookmakerType.SBOBET;
    }

    @Override
    public Optional<BookmakerMatchResponse> handle(BookmakerMatch bookmakerMatch) {
        log.debug("Start handling sbobet match: " + bookmakerMatch);
        Optional<BookmakerMatchResponse> bookmakerMatchResponse = findMatchInCache(bookmakerMatch);
        if (!bookmakerMatchResponse.isPresent()) {
            bookmakerMatchResponse = tryToFindMatch(bookmakerMatch);
        }

        return bookmakerMatchResponse
                .flatMap(mR -> downloadAndParseSingleMatch(bookmakerMatch, mR))
                .flatMap(
                        mR -> {
                            cache.add(mR);
                            return Optional.of(mR);
                        }
                );
    }

    // todo: переделать ( плохо )
    private Optional<BookmakerMatchResponse> tryToFindMatch(BookmakerMatch bookmakerMatch) {
        Match match = bookmakerMatch.getMatch();
        log.debug("Start parsing sport day page: sport={}, days from today={}", match.getSportType(), bookmakerMatch.getDaysFromToday());
        Document download = downloader.download(match.getSportType(), bookmakerMatch.getDaysFromToday());
        List<BookmakerMatchResponse> matches = getMatches(download);
        log.debug("Matches were parsed, amount: " + matches.size());
        cache.addAll(matches);
        return findMatchInCache(bookmakerMatch);
    }

    private Optional<BookmakerMatchResponse> findMatchInCache(BookmakerMatch bookmakerMatch) {
        return cache.find(bookmakerMatch);
    }

    private Optional<BookmakerMatchResponse> downloadAndParseSingleMatch(BookmakerMatch bookmakerMatch, BookmakerMatchResponse bookmakerMatchResponse) {
        Document document = downloader.download(bookmakerMatchResponse.getBookmakerMatchId(), bookmakerMatch);
        JSONArray arrayContainer = extractArrayFromHtml(document);
        JSONArray prematchArrays = arrayContainer.getJSONArray(MATCHES_ARRAYS_INDEX);
        List<BookmakerMatchResponse> bookmakerMatchResponses = getMatchesFromArray(prematchArrays.getJSONArray(0).getJSONArray(MATCHES_IN_CONTAINER_INDEX));
        if (bookmakerMatchResponses.isEmpty() || bookmakerMatchResponses.size() != 1) {
            throw new IllegalStateException("check");
        }
        return Optional.of(bookmakerMatchResponses.get(0));
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

    private JSONArray extractArrayFromHtml(Document download) {
        Element script = download.getElementsByTag("script").last();
        String scriptText = script.childNodes().get(0).toString();
        String jsArray = scriptText.substring(scriptText.indexOf("onUpdate('od',") + 14, scriptText.indexOf("); }"));
        return new JSONArray(jsArray);
    }

    private static final int MATCH_HANDICAP = 1;

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

            List<BookmakerCoeff> bookmakerCoeffs = new ArrayList<>();
            // j = 1, because, first element is info array
            for (int j = 1; j < matchCoeffsArray.length(); j++) {
                JSONArray coeffArrayContainer = matchCoeffsArray.getJSONArray(j);
                JSONArray handicapValueArray = coeffArrayContainer.getJSONArray(1);

                int betType = handicapValueArray.getInt(0);
                double handicapValue = handicapValueArray.getDouble(5);
                if (MATCH_HANDICAP == betType) {
                    JSONArray coeffValueArray = coeffArrayContainer.getJSONArray(2);
                    double homeCoeffValue = coeffValueArray.getDouble(HOME_COEFF_INDEX);
                    double guestCoeffValue = coeffValueArray.getDouble(GUEST_COEFF_INDEX);
                    BookmakerCoeff homeCoeff = BookmakerCoeff.of(-handicapValue, homeCoeffValue, HANDICAP, HOME, MATCH);
                    BookmakerCoeff guestCoeff = BookmakerCoeff.of(handicapValue, guestCoeffValue, HANDICAP, AWAY, MATCH);
                    bookmakerCoeffs.add(homeCoeff);
                    bookmakerCoeffs.add(guestCoeff);
                }
            }
            if (!bookmakerCoeffs.isEmpty()) {
                BookmakerMatchResponse bookmakerMatchResponse = BookmakerMatchResponse.builder()
                        .bookmakerType(getBookmakerType())
                        .bookmakerCoeffs(bookmakerCoeffs)
                        .bookmakerAwayTeamName(guestTeamName)
                        .bookmakerHomeTeamName(homeTeamName)
                        .bookmakerLeagueId(String.valueOf(sbobetLeagueId))
                        .bookmakerMatchId(String.valueOf(matchId))
                        .build();

                log.debug("Match was parsed: " + bookmakerMatchResponse);
                bookmakerMatchResponses.add(bookmakerMatchResponse);
            }
        }
        return bookmakerMatchResponses;
    }
}
