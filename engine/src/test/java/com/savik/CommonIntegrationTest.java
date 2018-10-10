package com.savik;

import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import com.savik.domain.SportType;
import com.savik.domain.Team;
import com.savik.events.ForkFoundEvent;
import com.savik.http.HttpClient;
import com.savik.model.Bet;
import com.savik.model.BookmakerCoeff;
import com.savik.service.EngineService;
import com.savik.service.bookmaker.BookmakerMatchService;
import com.savik.service.bookmaker.ForksService;
import com.savik.service.bookmaker.matchbook.MatchbookConfig;
import com.savik.service.bookmaker.pinnacle.PinnacleConfig;
import com.savik.service.bookmaker.sbobet.SbobetConfig;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.savik.domain.BookmakerType.MATCHBOOK;
import static com.savik.domain.BookmakerType.PINNACLE;
import static com.savik.domain.BookmakerType.SBOBET;
import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.COMMON;
import static com.savik.service.bookmaker.CoeffType.DRAW;
import static com.savik.service.bookmaker.CoeffType.FIRST_HALF;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.HOME_OR_AWAY;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static com.savik.service.bookmaker.CoeffType.OVER;
import static com.savik.service.bookmaker.CoeffType.TOTAL;
import static com.savik.service.bookmaker.CoeffType.UNDER;
import static com.savik.service.bookmaker.CoeffType.WIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@IntegrationTest
public class CommonIntegrationTest {


    @Autowired
    EngineService engineService;

    @Autowired
    PinnacleConfig pinnacleConfig;

    @Autowired
    SbobetConfig sbobetConfig;

    @Autowired
    MatchbookConfig matchbookConfig;
    
    @Autowired
    BookmakerMatchService bookmakerMatchService;

    @MockBean
    HttpClient httpClient;

    @MockBean
    ForksService forksService;

    List<Match> matches;

    Match FRANCE_PERU;
    Match LOKOMOTIV_SCHALKE;

    @BeforeEach
    public void init() throws URISyntaxException, IOException {
        FRANCE_PERU = Match.builder()
                .homeTeam(Team.builder().flashscoreId("QkGeVG1n").name("France").build())
                .awayTeam(Team.builder().flashscoreId("Uend67D3").name("Peru").build())
                .flashscoreLeagueId("lvUBR5F8")
                .sportType(SportType.FOOTBALL)
                .matchStatus(MatchStatus.PREMATCH)
                .date(LocalDateTime.of(2018, 6, 21, 15, 0))
                .flashscoreId("vHWXsRjb")
                .build();

        LOKOMOTIV_SCHALKE = Match.builder()
                .flashscoreId("dQGGBkla")
                .flashscoreLeagueId("xGrwqq16")
                .sportType(SportType.FOOTBALL)
                .date(LocalDateTime.parse("2018-10-03T16:55"))
                .matchStatus(MatchStatus.PREMATCH)
                .homeTeam(Team.builder().name("Lokomotiv Moscow").flashscoreId("Sjs63WfK").sportType(SportType.FOOTBALL).build())
                .awayTeam(Team.builder().name("Schalke").flashscoreId("0Ija0Ej9").sportType(SportType.FOOTBALL).build())
                .build();

        matches = Arrays.asList(FRANCE_PERU, LOKOMOTIV_SCHALKE);

        // matchbook football
        when(httpClient.getPinnacleApacheJson(eq(matchbookConfig.getEventsUrl(SportType.FOOTBALL))))
                .thenReturn(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("matchbook_football_events.json").toURI()))));

        // pinnacle football
        when(httpClient.getPinnacleApacheJson(eq(pinnacleConfig.getFixtureUrl(SportType.FOOTBALL)), any(Map.class)))
                .thenReturn(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pinnacle_football_fixtures.json").toURI()))));
        when(httpClient.getPinnacleApacheJson(eq(pinnacleConfig.getOddsUrl(SportType.FOOTBALL, null)), any(Map.class)))
                .thenReturn(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pinnacle_football_odds.json").toURI()))));

        // sbobet football
        when(httpClient.getAntibot(sbobetConfig.getSportUrl(SportType.FOOTBALL, FRANCE_PERU.getDaysFromToday())))
                .thenReturn(Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("sbobet_football_france_peru_day.html").toURI())))));
        when(httpClient.getAntibot(sbobetConfig.getSportUrl(SportType.FOOTBALL, LOKOMOTIV_SCHALKE.getDaysFromToday())))
                .thenReturn(Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("sbobet_football_lokomotiv_schalke_day.html").toURI())))));
        
        when(httpClient.getAntibot(sbobetConfig.getMatchUrl("2276353", bookmakerMatchService.createFromMatch(FRANCE_PERU, SBOBET).get())))
                .thenReturn(Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("sbobet_football_match_france_peru.html").toURI())))));
        when(httpClient.getAntibot(sbobetConfig.getMatchUrl("2276354", bookmakerMatchService.createFromMatch(LOKOMOTIV_SCHALKE, SBOBET).get())))
                .thenReturn(Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("sbobet_football_match_lokomotiv_schalke.html").toURI())))));
    }

    @Test
    public void test() {
        CompletableFuture<Void> future = engineService.handle(matches);
        future.join();
        ArgumentCaptor<List> francePeruCapture = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List> lokomotivSchalkeCapture = ArgumentCaptor.forClass(List.class);
        verify(forksService, times(1)).verifyExistence(eq(FRANCE_PERU), francePeruCapture.capture());
        verify(forksService, times(1)).verifyExistence(eq(LOKOMOTIV_SCHALKE), lokomotivSchalkeCapture.capture());
        final List<ForkFoundEvent> francePeruForks = francePeruCapture.getValue();
        assertEquals(9, francePeruForks.size());
        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(SBOBET, BookmakerCoeff.of(-1.25, 2.51, MATCH, HOME, HANDICAP)),
                        new Bet(PINNACLE, BookmakerCoeff.of(1.25, 1.69, MATCH, AWAY, HANDICAP))
                )
        ));
        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(SBOBET, BookmakerCoeff.of(-0.5, 2.19, FIRST_HALF, HOME, HANDICAP)),
                        new Bet(PINNACLE, BookmakerCoeff.of(0.5, 1.88, FIRST_HALF, AWAY, HANDICAP))
                )
        ));
        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(2.5, 2.09, MATCH, COMMON, TOTAL, OVER)),
                        new Bet(SBOBET, BookmakerCoeff.of(2.5, 1.93, MATCH, COMMON, TOTAL, UNDER))
                )
        ));

        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(1.75, 2.17, MATCH, HOME, TOTAL, OVER)),
                        new Bet(SBOBET, BookmakerCoeff.of(1.75, 1.91, MATCH, HOME, TOTAL, UNDER))
                )
        ));

        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(0.5, 2.0, MATCH, AWAY, TOTAL, OVER)),
                        new Bet(SBOBET, BookmakerCoeff.of(0.75, 2.01, MATCH, AWAY, TOTAL, UNDER))
                )
        ));

        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(1., 2.25, FIRST_HALF, COMMON, TOTAL, OVER)),
                        new Bet(SBOBET, BookmakerCoeff.of(1., 1.93, FIRST_HALF, COMMON, TOTAL, UNDER))
                )
        ));

        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(0.75, 1.95, FIRST_HALF, HOME, TOTAL, UNDER)),
                        new Bet(SBOBET, BookmakerCoeff.of(0.75, 2.09, FIRST_HALF, HOME, TOTAL, OVER))
                )
        ));

        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(0.5, 1.95, FIRST_HALF, AWAY, TOTAL, UNDER)),
                        new Bet(SBOBET, BookmakerCoeff.of(0.5, 3.12, FIRST_HALF, AWAY, TOTAL, OVER))
                )
        ));

        assertTrue(francePeruForks.contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(4.2, MATCH, DRAW)),
                        new Bet(SBOBET, BookmakerCoeff.of(1.35, MATCH, HOME_OR_AWAY))
                )
        ));


        final List<ForkFoundEvent> lokomotivSchalkeForks = lokomotivSchalkeCapture.getValue();
        assertEquals(3, lokomotivSchalkeForks.size());


        assertTrue(lokomotivSchalkeForks.contains(
                new ForkFoundEvent(
                        LOKOMOTIV_SCHALKE,
                        new Bet(PINNACLE, BookmakerCoeff.of(2., 1.884, MATCH, COMMON, TOTAL, OVER)),
                        new Bet(MATCHBOOK, BookmakerCoeff.of(2., 2.17, MATCH, COMMON, TOTAL, UNDER))
                )
        ));

        assertTrue(lokomotivSchalkeForks.contains(
                new ForkFoundEvent(
                        LOKOMOTIV_SCHALKE,
                        new Bet(PINNACLE, BookmakerCoeff.of(1.5, 4., FIRST_HALF, COMMON, TOTAL, OVER)),
                        new Bet(MATCHBOOK, BookmakerCoeff.of(1.5, 1.342466, FIRST_HALF, COMMON, TOTAL, OVER).lay()) // lay 3.92
                )
        ));

        assertTrue(lokomotivSchalkeForks.contains(
                new ForkFoundEvent(
                        LOKOMOTIV_SCHALKE,
                        new Bet(PINNACLE, BookmakerCoeff.of(3.9, MATCH, HOME, WIN)),
                        new Bet(MATCHBOOK, BookmakerCoeff.of(1.34965, MATCH, HOME, WIN).lay()) // lay 3.86
                )
        ));

    }
}