package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.BookmakerType;
import com.savik.domain.SportType;
import com.savik.service.bookmaker.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.CoeffType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.savik.service.bookmaker.BookmakerCoeff.of;
import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.FIRST_HALF;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static com.savik.service.bookmaker.CoeffType.OVER;
import static com.savik.service.bookmaker.CoeffType.SECOND_HALF;
import static com.savik.service.bookmaker.CoeffType.TOTAL;
import static com.savik.service.bookmaker.CoeffType.UNDER;

@Service
@Log4j2
public class PinnacleApi {

    @Autowired
    PinnacleDownloader downloader;

    Map<SportType, FixtureResponse> fixtures = new ConcurrentHashMap<>();
    Map<SportType, OddsResponse> odds = new ConcurrentHashMap<>();


    public Optional<BookmakerMatchResponse> parseMatch(BookmakerMatch match) {
        FixtureResponse fixture = getFixtureBySportType(match.getMatch().getSportType());
        Optional<FixtureEvent> event = findMatch(fixture, match);
        if (event.isPresent()) {
            log.debug(String.format("Fixture event was found: %s", event.get()));
            BookmakerMatchResponse bookmakerMatchResponse = parseMatch(event.get(), match);
            return Optional.of(bookmakerMatchResponse);
        } else {
            log.debug(String.format("Fixture event wasn't found, match flashscore id: %s", match.getMatch().getFlashscoreId()));
        }
        return Optional.empty();
    }

    private FixtureResponse getFixtureBySportType(SportType sportType) {
        FixtureResponse sportFixture = fixtures.get(sportType);
        if (sportFixture == null) {
            sportFixture = downloader.downloadFixtures(sportType);
            fixtures.put(sportType, sportFixture);
        }
        if (sportFixture == null) {
            throw new PinnacleException("fixture not found for sport type: " + sportType);
        }
        return sportFixture;
    }

    private Optional<FixtureEvent> findMatch(FixtureResponse fixture, BookmakerMatch match) {
        List<FixtureLeague> league = fixture.getLeague();

        FixtureLeague fixtureLeague = fixture.getLeague().stream()
                .filter(l -> Objects.equals(l.getId(), match.getBookmakerLeague().getBookmakerId()))
                .findFirst().get();

        Optional<FixtureEvent> matchFixture = fixtureLeague.getEvents().stream()
                .filter(event -> event.getHome().equals(match.getHomeTeam().getName()) &&
                        event.getAway().equals(match.getGuestTeam().getName()))
                .findFirst();

        return matchFixture;
    }

    private BookmakerMatchResponse parseMatch(FixtureEvent event, BookmakerMatch bookmakerMatch) {
        OddsResponse oddsResponse = getOddsResponseBySportType(bookmakerMatch.getMatch().getSportType());
        OddsEvent odds = oddsResponse.findEvent(event);
        List<BookmakerCoeff> bookmakerCoeffs = new ArrayList<>();
        List<OddsPeriod> periods = odds.getPeriods();
        for (OddsPeriod period : periods) {
            CoeffType partType = getPartByStatusCode(period.getStatus());
            List<OddsSpread> spreads = period.getSpreads();
            if (spreads != null) {
                for (OddsSpread spread : spreads) {
                    bookmakerCoeffs.add(of(spread.getHdp(), spread.getHome(), HANDICAP, HOME, partType));
                    bookmakerCoeffs.add(of(-spread.getHdp(), spread.getAway(), HANDICAP, AWAY, partType));
                }
            }
            List<OddsTotal> totals = period.getTotals();
            if (totals != null) {
                for (OddsTotal total : totals) {
                    bookmakerCoeffs.add(of(total.getPoints(), total.getOver(), OVER, TOTAL, partType));
                    bookmakerCoeffs.add(of(total.getPoints(), total.getUnder(), UNDER, TOTAL, partType));
                }
            }
            OddsTeamTotalBlock teamTotal = period.getTeamTotal();
            if (teamTotal != null) {
                OddsTeamTotal home = teamTotal.getHome();
                if (home != null) {
                    bookmakerCoeffs.add(of(home.getPoints(), home.getOver(), OVER, TOTAL, HOME, partType));
                    bookmakerCoeffs.add(of(home.getPoints(), home.getUnder(), UNDER, TOTAL, HOME, partType));
                }
                OddsTeamTotal away = teamTotal.getAway();
                if (home != null) {
                    bookmakerCoeffs.add(of(away.getPoints(), away.getOver(), OVER, TOTAL, AWAY, partType));
                    bookmakerCoeffs.add(of(away.getPoints(), away.getUnder(), UNDER, TOTAL, AWAY, partType));
                }
            }
        }

        BookmakerMatchResponse bookmakerMatchResponse = BookmakerMatchResponse.builder()
                .bookmakerHomeTeamName(bookmakerMatch.getHomeTeam().getName())
                .bookmakerAwayTeamName(bookmakerMatch.getGuestTeam().getName())
                .bookmakerLeagueId(bookmakerMatch.getBookmakerLeague().getBookmakerId())
                .bookmakerCoeffs(bookmakerCoeffs)
                .bookmakerType(BookmakerType.PINNACLE)
                .build();


        return bookmakerMatchResponse;
    }

    private CoeffType getPartByStatusCode(int status) {
        switch (status) {
            case 0:
                return MATCH;
            case 1:
                return FIRST_HALF;
            case 2:
                return SECOND_HALF;
        }
        throw new PinnacleException("Status is invalid, should be 1,2,3. Status: " + status);
    }

    private OddsResponse getOddsResponseBySportType(SportType sportType) {
        OddsResponse oddsResponse = odds.get(sportType);
        if (oddsResponse == null) {
            oddsResponse = downloader.downloadOdds(sportType);
            odds.put(sportType, oddsResponse);
        }
        if (oddsResponse == null) {
            throw new PinnacleException("odds not found for sport type: " + sportType);
        }
        return oddsResponse;
    }
}
