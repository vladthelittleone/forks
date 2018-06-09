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
        /*matches.add(
                Match.builder()
                        .flashscoreLeagueId("lrCpHADk")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Czech Republic TR10").flashscoreId("OvS60rcH").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("World TR10").flashscoreId("2VQAa2CN").sportType(FOOTBALL).build())
                        .build()
        );

         matches.add(
                Match.builder()
                        .flashscoreLeagueId("f1GbGBCd")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Latvia").flashscoreId("WC2jLpW4").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Azerbaijan").flashscoreId("UTJj1saj").sportType(FOOTBALL).build())
                        .build()
        );
        */

        matches.add(
                Match.builder()
                        .flashscoreId("f1GbGBCd")
                        .flashscoreLeagueId("f1GbGBCd")
                        .date(LocalDateTime.now())
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Hungary").flashscoreId("MTVhw6Go").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Australia").flashscoreId("xSrf6qMM").sportType(FOOTBALL).build())
                        .build()
        );
       /*
        matches.add(
                Match.builder()
                        .flashscoreLeagueId("A3LtWQ9B")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Estonia").flashscoreId("YJwM4R15").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Morocco").flashscoreId("IDKYO3R8").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreLeagueId("CpsMuFcR")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Finland").flashscoreId("QuVpPpic").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Belarus").flashscoreId("lhXEH2VT").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreLeagueId("I5dN11j8")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Serbia").flashscoreId("8Kl6iq0i").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Bolivia").flashscoreId("nXdi7mbc").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreLeagueId("tzQNtc5i")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Sweden").flashscoreId("OQyqbHWB").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Peru").flashscoreId("Uend67D3").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreLeagueId("jLPyK9Z6")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Denmark").flashscoreId("0KUdxQVi").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Mexico").flashscoreId("O6iHcNkd").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreLeagueId("dMdR34c8")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Spain").flashscoreId("bLyo6mco").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Tunisia").flashscoreId("QqZVYk95").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreLeagueId("tONSOAQ7")
                        .sportType(FOOTBALL)
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("France").flashscoreId("QkGeVG1n").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("USA").flashscoreId("fuitL4CF").sportType(FOOTBALL).build())
                        .build()
        );*/
        return matches;
    }
}

