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
                        .flashscoreId("dtgWPhVC")
                        .flashscoreLeagueId("lvUBR5F8")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.now())
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Russia").flashscoreId("hrgrswHh").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Saudi Arabia").flashscoreId("biSY8ox4").sportType(FOOTBALL).build())
                        .build()
        );
        return matches;
    }
}

