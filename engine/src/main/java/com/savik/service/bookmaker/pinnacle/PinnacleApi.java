package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.BookmakerType;
import com.savik.domain.MatchStatus;
import com.savik.domain.SportType;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Log4j2
class PinnacleApi {

    @Autowired
    PinnacleDownloader downloader;

    @Autowired
    PinnacleCache cache;

    @Autowired
    PinnacleParser parser;

    public Optional<BookmakerMatchResponse> parseMatch(BookmakerMatchWrapper match) {
        Optional<FixtureEvent> event = getEvent(match);
        if (event.isPresent()) {
            log.debug(String.format("Fixture event was found: %s, %s", match.getDefaultLogString(), event.get().getId()));
            BookmakerMatchResponse bookmakerMatchResponse = parseMatch(event.get(), match);
            log.debug(String.format("Fixture event was parsed: %s", match.getDefaultLogString()));
            return Optional.of(bookmakerMatchResponse);
        } else {
            log.info(String.format("Fixture event wasn't found, match flashscore id: %s, %s",
                    match.getDefaultLogString(), match.getBookmakerLeague()));
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

    private Optional<FixtureEvent> getEvent(BookmakerMatchWrapper match) {
        FixtureResponse fixture = getFixtureBySportType(match.getMatch().getSportType());
        return findMatch(fixture, match);
    }

    private synchronized FixtureResponse getFixtureBySportType(SportType sportType) {
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

    private Optional<FixtureEvent> findMatch(FixtureResponse fixture, BookmakerMatchWrapper match) {
        FixtureLeague fixtureLeague = fixture.getLeague().stream()
                .filter(l -> Objects.equals(l.getId(), Integer.parseInt(match.getBookmakerLeague().getBookmakerId())))
                .findFirst().get();

        Predicate<FixtureEvent> predicate = event -> event.getHome().equalsIgnoreCase(match.getHomeTeam().getName()) &&
                event.getAway().equalsIgnoreCase(match.getAwayTeam().getName()) &&
                //event.getStatus().equals("I") &&
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

    private BookmakerMatchResponse parseMatch(FixtureEvent event, BookmakerMatchWrapper bookmakerMatchWrapper) {
        log.trace(String.format("Start parse event. event:%s", event));
        OddsResponse oddsResponse = getOddsResponseBySportType(bookmakerMatchWrapper.getMatch().getSportType());
        OddsEvent odds = oddsResponse.findEvent(event);

        BookmakerMatchResponse bookmakerMatchResponse = BookmakerMatchResponse.builder()
                .bookmakerHomeTeamName(bookmakerMatchWrapper.getHomeTeam().getName())
                .bookmakerAwayTeamName(bookmakerMatchWrapper.getAwayTeam().getName())
                .bookmakerLeagueId(bookmakerMatchWrapper.getBookmakerLeague().getBookmakerId())
                .bookmakerCoeffs(new ArrayList<>(parser.parse(odds)))
                .bookmakerType(BookmakerType.PINNACLE)
                .build();
        log.trace(String.format("Event was parsed. event:%s", event));
        return bookmakerMatchResponse;
    }

    private synchronized OddsResponse getOddsResponseBySportType(SportType sportType) {
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
