package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.SportType;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class PinnacleApi {

    @Autowired
    PinnacleDownloader downloader;

    Map<SportType, FixtureResponse> fixtures = new ConcurrentHashMap<>();
    Map<SportType, OddsResponse> odds = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
    }

    public Optional<BookmakerMatchResponse> parseMatch(BookmakerMatch match) {
        FixtureResponse fixture = getFixtureBySportType(match.getMatch().getSportType());
        Optional<FixtureEvent> event = findMatch(fixture, match);
        if(event.isPresent()) {
            log.debug(String.format("Fixture event was found: %s", event.get()));
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

    private BookmakerMatchResponse parseMatch(FixtureEvent event, SportType sportType) {
        OddsResponse oddsResponse = getOddsResponseBySportType(sportType);

        oddsResponse.getLeague();


        return null;
    }

    private OddsResponse getOddsResponseBySportType(SportType sportType) {
        OddsResponse oddsResponse = odds.get(sportType);
        if(oddsResponse == null) {
            oddsResponse = downloader.downloadOdds(sportType);
            odds.put(sportType, oddsResponse);
        }
        return oddsResponse;
    }
}
