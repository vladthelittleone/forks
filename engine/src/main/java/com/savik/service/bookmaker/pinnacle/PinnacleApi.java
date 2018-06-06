package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.SportType;
import com.savik.service.bookmaker.BookmakerMatch;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PinnacleApi {

    Map<SportType, FixtureResponse> fixtures = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
    }

    public void parseMatch(BookmakerMatch match) {
        FixtureResponse fixtureResponse = fixtures.get(match.getMatch().getSportType());



    }
}
