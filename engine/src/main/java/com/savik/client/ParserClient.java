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

import static com.savik.domain.MatchStatus.LIVE;
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
                        .flashscoreId("lElsTCM2")
                        .flashscoreLeagueId("dYlOSQOD")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Everton").flashscoreId("KluSTr9s").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Southampton").flashscoreId("WdKOwxDM").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("S4moSWy9")
                        .flashscoreLeagueId("dYlOSQOD")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Leicester").flashscoreId("KrrdAMyI").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Wolves").flashscoreId("j3Azpf5d").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("zVjgQA6L")
                        .flashscoreLeagueId("dYlOSQOD")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Tottenham").flashscoreId("UDg08Ohm").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Fulham").flashscoreId("69ZiU2Om").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("IgtbPULR")
                        .flashscoreLeagueId("dYlOSQOD")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("West Ham").flashscoreId("Cxq57r8g").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Bournemouth").flashscoreId("OtpNdwrc").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("p6wf6AE0")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Bristol City").flashscoreId("MahqU27I").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Middlesbrough").flashscoreId("6F0lvLNC").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("Odxb5UT6")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Hull City").flashscoreId("S66R0t75").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Blackburn").flashscoreId("6Nl8nagD").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("IJy24lqD")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Ipswich").flashscoreId("thqhB2MB").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Aston Villa").flashscoreId("W00wmLO0").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("G4f538bJ")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Leeds").flashscoreId("tUxUbLR2").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Rotherham").flashscoreId("KMfM7P4b").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("KxLoC02J")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Millwall").flashscoreId("6uz2cJBL").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Derby").flashscoreId("Iug0YO0Q").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("jLBH6vQt")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Reading").flashscoreId("EmlVDpVP").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Bolton").flashscoreId("Or1bBrWD").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("UBAL5bum")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Sheffield Utd").flashscoreId("MeKPSerA").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Norwich").flashscoreId("Qo6off6p").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("Cz0Q4Ifg")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("West Brom").flashscoreId("CCBWpzjj").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("QPR").flashscoreId("lrMHUHDc").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("Qw4U3xAa")
                        .flashscoreLeagueId("2DSCa5fE")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Wigan").flashscoreId("MgHlK2Na").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Nottingham").flashscoreId("UsushcZr").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("zouG0vTn")
                        .flashscoreLeagueId("rJSMG3H0")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Barnsley").flashscoreId("UNGEKMx6").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("AFC Wimbledon").flashscoreId("4KREuF43").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("llqKabrg")
                        .flashscoreLeagueId("rJSMG3H0")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Burton").flashscoreId("nwCtJaJT").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Doncaster").flashscoreId("hlzYDy5q").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("WCYObIca")
                        .flashscoreLeagueId("rJSMG3H0")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Coventry").flashscoreId("GOvB22xg").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Plymouth").flashscoreId("WpGM6ELj").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("4vnWddSB")
                        .flashscoreLeagueId("rJSMG3H0")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Peterborough").flashscoreId("tWeI85kh").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Luton").flashscoreId("SlybbadF").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("QgzuezcO")
                        .flashscoreLeagueId("rJSMG3H0")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Shrewsbury").flashscoreId("EqfQ6qK4").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Blackpool").flashscoreId("8EFIJthC").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("vcvqffCU")
                        .flashscoreLeagueId("rJSMG3H0")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Southend").flashscoreId("tGwiyvJ1").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Bradford City").flashscoreId("bsc3EJ27").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("f9EhEccn")
                        .flashscoreLeagueId("rJSMG3H0")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Walsall").flashscoreId("YgrfcLuL").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Gillingham").flashscoreId("WvZyDeKk").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("Cp9cDHCh")
                        .flashscoreLeagueId("rJSMG3H0")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Wycombe").flashscoreId("hl2V5JIl").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Bristol Rovers").flashscoreId("GILi6JC9").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("Mg5fHXfb")
                        .flashscoreLeagueId("0MwU4NW6")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Bury").flashscoreId("bVpINVLD").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Forest Green").flashscoreId("v7ZNnsJG").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("0p4bGiA4")
                        .flashscoreLeagueId("0MwU4NW6")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Cheltenham").flashscoreId("jizXHcsM").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Carlisle").flashscoreId("dUzTIwSF").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("YH2OkMy4")
                        .flashscoreLeagueId("0MwU4NW6")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Exeter").flashscoreId("ve14a3l4").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Newport").flashscoreId("ChwgcrNJ").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("jZdBDkfN")
                        .flashscoreLeagueId("0MwU4NW6")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Mansfield").flashscoreId("t6G9wMZN").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Colchester").flashscoreId("xYu73rNn").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("lKFEC99T")
                        .flashscoreLeagueId("0MwU4NW6")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Northampton").flashscoreId("l4P6dwRR").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Cambridge Utd").flashscoreId("lnb8EJRp").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("Yiio7mXj")
                        .flashscoreLeagueId("0MwU4NW6")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Oldham").flashscoreId("O4voiHlk").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Macclesfield").flashscoreId("WYFXQ1KH").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("rNYyUAv4")
                        .flashscoreLeagueId("0MwU4NW6")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Swindon").flashscoreId("xSloasB8").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Tranmere").flashscoreId("jVhkb1QE").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("v7qany2B")
                        .flashscoreLeagueId("l4P6dwRR")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Dumbarton").flashscoreId("S6Xv3lj1").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Arbroath").flashscoreId("prrz4Uze").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("Y5x8pFWN")
                        .flashscoreLeagueId("l4P6dwRR")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Montrose").flashscoreId("vinuwjSQ").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Brechin").flashscoreId("OORsaEb2").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("SSyCqZoU")
                        .flashscoreLeagueId("l4P6dwRR")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Raith").flashscoreId("YJUiAWcR").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("East Fife").flashscoreId("Y5sGF857").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("UyOwugfo")
                        .flashscoreLeagueId("l4P6dwRR")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-08-18T14:00"))
                        .matchStatus(LIVE)
                        .homeTeam(Team.builder().name("Stenhousemuir").flashscoreId("dWY6Y9zr").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Airdrieonians").flashscoreId("KWzLBhEq").sportType(FOOTBALL).build())
                        .build()
        );
        return matches;
    }
}

