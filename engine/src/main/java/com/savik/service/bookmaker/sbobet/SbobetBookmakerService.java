package com.savik.service.bookmaker.sbobet;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import com.savik.service.bookmaker.CoeffType;
import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class SbobetBookmakerService extends BookmakerService {
    public static final int LIVE_AND_PREMATCH_INDEX = 2;
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

    private Map<Integer, List<BookmakerMatchResponse>> cache = new HashMap<>();

    @Autowired
    SbobetDownloader downloader;

    @Override
    protected BookmakerType getBookmakerType() {
        return BookmakerType.SBOBET;
    }

    @Override
    public Optional<BookmakerMatchResponse> handle(BookmakerMatch bookmakerMatch) {
        Optional<BookmakerMatchResponse> bookmakerMatchResponse = findMatch(bookmakerMatch);
        if (!bookmakerMatchResponse.isPresent()) {
            Match match = bookmakerMatch.getMatch();
            Document download = downloader.download(match.getSportType(), bookmakerMatch.getDaysFromToday());
            List<BookmakerMatchResponse> matches = getMatches(download);
            cache.put(bookmakerMatch.getDaysFromToday(), matches);
            bookmakerMatchResponse = findMatch(bookmakerMatch);
        }

        return bookmakerMatchResponse.isPresent() ?
                downloadAndParseSingleMatch(bookmakerMatch, bookmakerMatchResponse.get()) :
                Optional.empty();
    }

    private Optional<BookmakerMatchResponse> findMatch(BookmakerMatch bookmakerMatch) {
        cache.putIfAbsent(bookmakerMatch.getDaysFromToday(), new ArrayList<>());
        List<BookmakerMatchResponse> cachedMatches = cache.get(bookmakerMatch.getDaysFromToday());
        for (BookmakerMatchResponse cachedMatch : cachedMatches) {
            BookmakerLeague bookmakerLeague = bookmakerMatch.getBookmakerLeague();
            BookmakerTeam homeTeam = bookmakerMatch.getHomeTeam();
            BookmakerTeam guestTeam = bookmakerMatch.getGuestTeam();
            if (Objects.equals(bookmakerLeague.getBookmakerId(), cachedMatch.getBookmakerLeagueId()) &&
                    Objects.equals(homeTeam.getName(), cachedMatch.getBookmakerHomeTeamName()) &&
                    Objects.equals(guestTeam.getName(), cachedMatch.getBookmakerGuestTeamName())) {
                return Optional.of(cachedMatch);
            }
        }
        return Optional.empty();
    }

    private Optional<BookmakerMatchResponse> downloadAndParseSingleMatch(BookmakerMatch bookmakerMatch, BookmakerMatchResponse bookmakerMatchResponse) {
        Document document = downloader.download(bookmakerMatchResponse, bookmakerMatch);
        JSONArray jsonArray = extractArrayFromHtml(document);
        return Optional.empty();
    }

    private List<BookmakerMatchResponse> getMatches(Document document) {
        JSONArray jsonArray = extractArrayFromHtml(document);
        List<BookmakerMatchResponse> bookmakerMatchResponses = new ArrayList<>();
        JSONArray liveAndPrematchArrays = jsonArray.getJSONArray(LIVE_AND_PREMATCH_INDEX);
        // live and prematch
        if (liveAndPrematchArrays.length() == 2) {
            getMatchesFromArray(liveAndPrematchArrays.getJSONArray(LIVE_MATCHES_INDEX)
                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX), bookmakerMatchResponses);
            getMatchesFromArray(liveAndPrematchArrays.getJSONArray(PREMATCH_MATCHES_WHILE_LIVE_EXISTS_INDEX)
                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX), bookmakerMatchResponses);
        } else if (liveAndPrematchArrays.length() == 1) {         // only prematch
            getMatchesFromArray(liveAndPrematchArrays.getJSONArray(PREMATCH_MATCHES_WHILE_LIVE_NOT_EXISTS_INDEX)
                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX), bookmakerMatchResponses);
        }
        return bookmakerMatchResponses;

    }

    private JSONArray extractArrayFromHtml(Document download) {
        Element script = download.getElementsByTag("script").last();
        String scriptText = script.childNodes().get(0).toString();

        String jsArray = scriptText.substring(scriptText.indexOf("onUpdate('od',") + 14, scriptText.indexOf("); }"));
        return new JSONArray(jsArray);
    }

    private void getMatchesFromArray(JSONArray matchesArray, List<BookmakerMatchResponse> bookmakerMatchResponses) {
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

                double handicapValue = handicapValueArray.getDouble(5);
                // 0 is 1x2 coeffs. useless for forks
                if (handicapValue != 0) {
                    JSONArray coeffValueArray = coeffArrayContainer.getJSONArray(2);
                    double homeCoeffValue = coeffValueArray.getDouble(HOME_COEFF_INDEX);
                    double guestCoeffValue = coeffValueArray.getDouble(GUEST_COEFF_INDEX);

                    BookmakerCoeff homeCoeff = new BookmakerCoeff(
                            CoeffType.HOME,
                            new BookmakerCoeff(
                                    CoeffType.MATCH,
                                    new BookmakerCoeff(CoeffType.HANDICAP, handicapValue, homeCoeffValue)
                            )
                    );

                    BookmakerCoeff guestCoeff = new BookmakerCoeff(
                            CoeffType.GUEST,
                            new BookmakerCoeff(
                                    CoeffType.MATCH,
                                    new BookmakerCoeff(CoeffType.HANDICAP, -handicapValue, guestCoeffValue)
                            )
                    );

                    bookmakerCoeffs.add(homeCoeff);
                    bookmakerCoeffs.add(guestCoeff);

                }
            }
            if (!bookmakerCoeffs.isEmpty()) {
                BookmakerMatchResponse bookmakerMatchResponse = BookmakerMatchResponse.builder()
                        .bookmakerCoeffs(bookmakerCoeffs)
                        .bookmakerGuestTeamName(guestTeamName)
                        .bookmakerHomeTeamName(homeTeamName)
                        .bookmakerLeagueId(String.valueOf(sbobetLeagueId))
                        .bookmakerMatchId(String.valueOf(matchId))
                        .build();

                bookmakerMatchResponses.add(bookmakerMatchResponse);


            }


        }
    }
}
