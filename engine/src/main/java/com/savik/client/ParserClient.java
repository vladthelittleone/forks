package com.savik.client;

import com.savik.domain.Match;
import com.savik.domain.Team;
import com.savik.filter.MatchFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.savik.domain.MatchStatus.PREMATCH;
import static com.savik.domain.SportType.FOOTBALL;

@Component
@FeignClient(value = "parser", fallback = ParserClientFallback.class)
public interface ParserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/matches")
    List<Match> getMatches(@RequestParam("matchFilter") MatchFilter matchFilter);
}

@Component
class ParserClientFallback implements ParserClient {

    @Override
    public List<Match> getMatches(MatchFilter matchFilter) {
        List<Match> matches = new ArrayList();
        matches.add(
                Match.builder()
                        .flashscoreId("8l2MGwih")
                        .flashscoreLeagueId("f1GbGBCd")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.now())
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Austria").flashscoreId("naHiWdnt").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Brazil").flashscoreId("I9l9aqLq").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("8jFLe3oC")
                        .flashscoreLeagueId("f1GbGBCd")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.now())
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("India").flashscoreId("A1UUGpep").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Kenya").flashscoreId("CIAJw1QK").sportType(FOOTBALL).build())
                        .build()
        );
        return matches;
    }
}

