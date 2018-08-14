package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.SportType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class PinnacleCache {

    Map<SportType, FixtureResponse> fixtures = new ConcurrentHashMap<>();
    Map<SportType, OddsResponse> odds = new ConcurrentHashMap<>();

    public synchronized FixtureResponse getFixture(SportType sportType) {
        return fixtures.get(sportType);
    }

    public synchronized void putFixture(SportType sportType, FixtureResponse fixtureResponse) {
        fixtures.put(sportType, fixtureResponse);
    }

    public synchronized OddsResponse getOdds(SportType sportType) {
        return odds.get(sportType);
    }

    public synchronized Long getOddsLastTimestamp(SportType sportType) {
        return odds.get(sportType).getLast();
    }

    public synchronized void putOdds(SportType sportType, OddsResponse oddsResponse) {
        odds.put(sportType, oddsResponse);
    }

    public synchronized Collection<SportType> getSportTypes() {
        return fixtures.keySet();
    }

    public synchronized void updateOdds(SportType sportType, OddsResponse deltaOdds) {
        final OddsResponse expiredOdds = odds.get(sportType);
        expiredOdds.setLast(deltaOdds.getLast());

        final List<OddsLeague> deltaLeagues = deltaOdds.getLeagues();
        for (OddsLeague deltaLeague : deltaLeagues) {
            final Optional<OddsLeague> optionalExpiredLeague = expiredOdds.findLeague(deltaLeague.getId());
            if (optionalExpiredLeague.isPresent()) {
                final OddsLeague expiredLeague = optionalExpiredLeague.get();
                final List<OddsEvent> deltaEvents = deltaLeague.getEvents();
                for (OddsEvent deltaEvent : deltaEvents) {
                    final Optional<OddsEvent> optionalExpiredEvent = expiredLeague.findEvent(deltaEvent.getId());
                    if (optionalExpiredEvent.isPresent()) {
                        final OddsEvent expiredEvent = optionalExpiredEvent.get();
                        final List<OddsPeriod> deltaPeriods = deltaEvent.getPeriods();
                        for (OddsPeriod deltaPeriod : deltaPeriods) {
                            expiredEvent.replacePeriod(deltaPeriod);
                        }
                    } else {
                        expiredLeague.addEvent(deltaEvent);
                    }
                }
            } else {
                expiredOdds.addLeague(deltaLeague);
            }
        }
    }
}
