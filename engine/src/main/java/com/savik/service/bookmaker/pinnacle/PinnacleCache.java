package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.SportType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class PinnacleCache {

    Map<SportType, FixtureResponse> fixtures = new ConcurrentHashMap<>();
    Map<SportType, OddsResponse> odds = new ConcurrentHashMap<>();
    
    public FixtureResponse getFixture(SportType sportType) {
        return fixtures.get(sportType);
    }
    
    public void putFixture(SportType sportType, FixtureResponse fixtureResponse) {
        fixtures.put(sportType, fixtureResponse);
    }
    
    public OddsResponse getOdds(SportType sportType) {
        return odds.get(sportType);
    }

    public void putOdds(SportType sportType, OddsResponse oddsResponse) {
        odds.put(sportType, oddsResponse);
    }
}
