package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.BookmakerType;
import com.savik.domain.MatchStatus;
import com.savik.domain.SportType;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.CoeffType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static com.savik.model.BookmakerCoeff.of;
import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.COMMON;
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

    @Autowired
    PinnacleCache cache;

    public Optional<BookmakerMatchResponse> parseMatch(BookmakerMatch match) {
        Optional<FixtureEvent> event = getEvent(match);
        if (event.isPresent()) {
            log.debug(String.format("Fixture event was found: %s", event.get()));
            BookmakerMatchResponse bookmakerMatchResponse = parseMatch(event.get(), match);
            return Optional.of(bookmakerMatchResponse);
        } else {
            log.info(String.format("Fixture event wasn't found, match flashscore id: %s", match.getMatch().getFlashscoreId()));
        }
        return Optional.empty();
    }

    public void refreshCacheOdds() {
        final Collection<SportType> sportTypes = cache.getSportTypes();
        for (SportType sportType : sportTypes) {
            final OddsResponse deltaOdds = downloader.downloadOdds(sportType, cache.getOddsLastTimestamp(sportType));
            cache.updateOdds(sportType, deltaOdds);
        }
    }

    private Optional<FixtureEvent> getEvent(BookmakerMatch match) {
        FixtureResponse fixture = getFixtureBySportType(match.getMatch().getSportType());
        return findMatch(fixture, match);
    }

    private FixtureResponse getFixtureBySportType(SportType sportType) {
        FixtureResponse sportFixture = cache.getFixture(sportType);
        if (sportFixture == null) {
            log.debug(String.format("Fixture wasn't found. Sport:%s", sportType));
            sportFixture = downloader.downloadFixtures(sportType);
            cache.putFixture(sportType, sportFixture);
            log.debug(String.format("Fixture was downloaded. Sport:%s", sportType));
        }
        if (sportFixture == null) {
            throw new PinnacleException("fixture not found for sport type: " + sportType);
        }
        return sportFixture;
    }

    private Optional<FixtureEvent> findMatch(FixtureResponse fixture, BookmakerMatch match) {
        FixtureLeague fixtureLeague = fixture.getLeague().stream()
                .filter(l -> Objects.equals(l.getId(), Integer.parseInt(match.getBookmakerLeague().getBookmakerId())))
                .findFirst().get();

        Predicate<FixtureEvent> predicate = event -> event.getHome().equals(match.getHomeTeam().getName()) &&
                event.getAway().equals(match.getAwayTeam().getName()) &&
                event.getStatus().equals("I") &&
                event.getStarts().toLocalDate().isEqual(match.getMatch().getDate().toLocalDate());

        if (match.getMatch().getMatchStatus() == MatchStatus.PREMATCH) {
            predicate = predicate.and(event -> (event.getLiveStatus() == 2 || event.getLiveStatus() == 0) &&
                    event.getParentId() == null);
        } else if (match.getMatch().getMatchStatus() == MatchStatus.LIVE) {
            predicate = predicate.and(event -> event.getLiveStatus() == 1 && event.getParentId() != null);
        } else {
            throw new IllegalArgumentException("Illegal match status: " + match);
        }
        Optional<FixtureEvent> matchFixture = fixtureLeague.getEvents().stream()
                .filter(predicate).findFirst();
        return matchFixture;
    }

    private BookmakerMatchResponse parseMatch(FixtureEvent event, BookmakerMatch bookmakerMatch) {
        log.debug(String.format("Start parse event. event:%s", event));
        OddsResponse oddsResponse = getOddsResponseBySportType(bookmakerMatch.getMatch().getSportType());
        OddsEvent odds = oddsResponse.findEvent(event);
        Set<BookmakerCoeff> bookmakerCoeffs = new HashSet<>();
        List<OddsPeriod> periods = odds.getPeriods();
        for (OddsPeriod period : periods) {
            CoeffType partType = getPartByStatusCode(period.getNumber());
            final OddsMoneyline moneyline = period.getMoneyline();
            if (moneyline != null) {
                if (moneyline.getHome() != null) {
                    bookmakerCoeffs.add(of(-0.5, moneyline.getHome(), partType, HOME, HANDICAP));
                }
                if (moneyline.getAway() != null) {
                    bookmakerCoeffs.add(of(-0.5, moneyline.getAway(), partType, AWAY, HANDICAP));
                }
            }
            List<OddsSpread> spreads = period.getSpreads();
            if (spreads != null) {
                for (OddsSpread spread : spreads) {
                    bookmakerCoeffs.add(of(spread.getHdp(), spread.getHome(), partType, HOME, HANDICAP));
                    bookmakerCoeffs.add(of(-spread.getHdp(), spread.getAway(), partType, AWAY, HANDICAP));
                }
            }
            List<OddsTotal> totals = period.getTotals();
            if (totals != null) {
                for (OddsTotal total : totals) {
                    bookmakerCoeffs.add(of(total.getPoints(), total.getOver(), partType, COMMON, TOTAL, OVER));
                    bookmakerCoeffs.add(of(total.getPoints(), total.getUnder(), partType, COMMON, TOTAL, UNDER));
                }
            }
            OddsTeamTotalBlock teamTotal = period.getTeamTotal();
            if (teamTotal != null) {
                OddsTeamTotal home = teamTotal.getHome();
                if (home != null) {
                    bookmakerCoeffs.add(of(home.getPoints(), home.getOver(), partType, HOME, TOTAL, OVER));
                    bookmakerCoeffs.add(of(home.getPoints(), home.getUnder(), partType, HOME, TOTAL, UNDER));
                }
                OddsTeamTotal away = teamTotal.getAway();
                if (home != null) {
                    bookmakerCoeffs.add(of(away.getPoints(), away.getOver(), partType, AWAY, TOTAL, OVER));
                    bookmakerCoeffs.add(of(away.getPoints(), away.getUnder(), partType, AWAY, TOTAL, UNDER));
                }
            }
        }

        BookmakerMatchResponse bookmakerMatchResponse = BookmakerMatchResponse.builder()
                .bookmakerHomeTeamName(bookmakerMatch.getHomeTeam().getName())
                .bookmakerAwayTeamName(bookmakerMatch.getAwayTeam().getName())
                .bookmakerLeagueId(bookmakerMatch.getBookmakerLeague().getBookmakerId())
                .bookmakerCoeffs(new ArrayList<>(bookmakerCoeffs))
                .bookmakerType(BookmakerType.PINNACLE)
                .build();

        log.debug(String.format("Event was parsed. event:%s", event));
        return bookmakerMatchResponse;
    }

    private CoeffType getPartByStatusCode(int number) {
        switch (number) {
            case 0:
                return MATCH;
            case 1:
                return FIRST_HALF;
            case 2:
                return SECOND_HALF;
        }
        throw new PinnacleException("number is invalid, should be 1,2,3. number: " + number);
    }

    private OddsResponse getOddsResponseBySportType(SportType sportType) {
        OddsResponse oddsResponse = cache.getOdds(sportType);
        if (oddsResponse == null) {
            log.debug(String.format("Odds not found. sport:%s", sportType));
            oddsResponse = downloader.downloadOdds(sportType, null);
            cache.putOdds(sportType, oddsResponse);
        }
        if (oddsResponse == null) {
            throw new PinnacleException("odds not found for sport type: " + sportType);
        }
        return oddsResponse;
    }
}
