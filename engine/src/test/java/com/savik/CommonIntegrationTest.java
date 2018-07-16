package com.savik;

import com.savik.domain.Match;
import com.savik.domain.SportType;
import com.savik.domain.Team;
import com.savik.events.ForkFoundEvent;
import com.savik.http.HttpClient;
import com.savik.model.Bet;
import com.savik.model.BookmakerCoeff;
import com.savik.service.EngineService;
import com.savik.service.bookmaker.BookmakerMatchService;
import com.savik.service.bookmaker.ForksListenerService;
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

import static com.savik.domain.BookmakerType.PINNACLE;
import static com.savik.domain.BookmakerType.SBOBET;
import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.COMMON;
import static com.savik.service.bookmaker.CoeffType.FIRST_HALF;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static com.savik.service.bookmaker.CoeffType.OVER;
import static com.savik.service.bookmaker.CoeffType.TOTAL;
import static com.savik.service.bookmaker.CoeffType.UNDER;
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
    BookmakerMatchService bookmakerMatchService;

    @MockBean
    HttpClient httpClient;

    @MockBean
    ForksListenerService forksListenerService;

    List<Match> matches;

    Match FRANCE_PERU;

    @BeforeEach
    public void init() throws URISyntaxException, IOException {
        FRANCE_PERU = Match.builder()
                .homeTeam(Team.builder().flashscoreId("QkGeVG1n").name("France").build())
                .awayTeam(Team.builder().flashscoreId("Uend67D3").name("Peru").build())
                .flashscoreLeagueId("lvUBR5F8")
                .sportType(SportType.FOOTBALL)
                .date(LocalDateTime.now())
                .flashscoreId("vHWXsRjb")
                .build();

        matches = Arrays.asList(FRANCE_PERU);

        // pinnacle football
        when(httpClient.downloadJson(eq(pinnacleConfig.getFixtureUrl(SportType.FOOTBALL)), any(Map.class)))
                .thenReturn(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pinnacle_football_fixtures.json").toURI()))));
        when(httpClient.downloadJson(eq(pinnacleConfig.getOddsUrl(SportType.FOOTBALL)), any(Map.class)))
                .thenReturn(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pinnacle_football_odds.json").toURI()))));

        // sbobet football
        when(httpClient.downloadAntibot(sbobetConfig.getSportUrl(SportType.FOOTBALL, 0)))
                .thenReturn(Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("sbobet_football.html").toURI())))));
        when(httpClient.downloadAntibot(sbobetConfig.getMatchUrl("2276353", bookmakerMatchService.createFromMatch(FRANCE_PERU, SBOBET).get())))
                .thenReturn(Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("sbobet_football_match_france_peru.html").toURI())))));
    }

    @Test
    public void test() {
        CompletableFuture<Void> future = engineService.handle(matches);
        future.join();
        ArgumentCaptor<ForkFoundEvent> argument = ArgumentCaptor.forClass(ForkFoundEvent.class);
        verify(forksListenerService, times(7)).handle(argument.capture());


        assertTrue(argument.getAllValues().contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(SBOBET, BookmakerCoeff.of(-1.25, 2.51, MATCH, HOME, HANDICAP)),
                        new Bet(PINNACLE, BookmakerCoeff.of(1.25, 1.69, MATCH, AWAY, HANDICAP))
                )
        ));
        assertTrue(argument.getAllValues().contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(SBOBET, BookmakerCoeff.of(-0.5, 2.19, FIRST_HALF, HOME, HANDICAP)),
                        new Bet(PINNACLE, BookmakerCoeff.of(0.5, 1.88, FIRST_HALF, AWAY, HANDICAP))
                )
        ));
        assertTrue(argument.getAllValues().contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(2.5, 2.09, MATCH, COMMON, TOTAL, OVER)),
                        new Bet(SBOBET, BookmakerCoeff.of(2.5, 1.93, MATCH, COMMON, TOTAL, UNDER))
                )
        ));
        
        assertTrue(argument.getAllValues().contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(1.75, 2.17, MATCH, HOME, TOTAL, OVER)),
                        new Bet(SBOBET, BookmakerCoeff.of(1.75, 1.91, MATCH, HOME, TOTAL, UNDER))
                )
        ));
        
        assertTrue(argument.getAllValues().contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(0.5, 2.0, MATCH, AWAY, TOTAL, OVER)),
                        new Bet(SBOBET, BookmakerCoeff.of(0.75, 2.01, MATCH, AWAY, TOTAL, UNDER))
                )
        ));
        
        assertTrue(argument.getAllValues().contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(1., 2.25, FIRST_HALF, COMMON, TOTAL, OVER)),
                        new Bet(SBOBET, BookmakerCoeff.of(1., 1.93, FIRST_HALF, COMMON, TOTAL, UNDER))
                )
        ));
        
        assertTrue(argument.getAllValues().contains(
                new ForkFoundEvent(
                        FRANCE_PERU,
                        new Bet(PINNACLE, BookmakerCoeff.of(0.75, 1.95, FIRST_HALF, HOME, TOTAL, UNDER)),
                        new Bet(SBOBET, BookmakerCoeff.of(0.75, 2.09, FIRST_HALF, HOME, TOTAL, OVER))
                )
        ));
    }
}