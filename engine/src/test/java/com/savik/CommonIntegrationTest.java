package com.savik;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.domain.SportType;
import com.savik.domain.Team;
import com.savik.http.Downloader;
import com.savik.service.EngineService;
import com.savik.service.bookmaker.BookmakerMatchService;
import com.savik.service.bookmaker.pinnacle.PinnacleConfig;
import com.savik.service.bookmaker.sbobet.SbobetConfig;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    Downloader downloader;

    List<Match> matches;

    @BeforeEach
    public void init() throws URISyntaxException, IOException {
        Match match = Match.builder()
                .homeTeam(Team.builder().flashscoreId("QkGeVG1n").name("France").build())
                .awayTeam(Team.builder().flashscoreId("Uend67D3").name("Peru").build())
                .flashscoreLeagueId("lvUBR5F8")
                .sportType(SportType.FOOTBALL)
                .date(LocalDateTime.now())
                .flashscoreId("vHWXsRjb")
                .build();

        matches = Arrays.asList(match);

        // pinnacle football
        when(downloader.downloadJson(eq(pinnacleConfig.getFixtureUrl(SportType.FOOTBALL)), any(Map.class)))
                .thenReturn(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pinnacle_football_fixtures.json").toURI()))));
        when(downloader.downloadJson(eq(pinnacleConfig.getOddsUrl(SportType.FOOTBALL)), any(Map.class)))
                .thenReturn(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pinnacle_football_odds.json").toURI()))));

        // sbobet football
        when(downloader.downloadAntibot(sbobetConfig.getSportUrl(SportType.FOOTBALL, 0)))
                .thenReturn(Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("sbobet_football.html").toURI())))));
        when(downloader.downloadAntibot(sbobetConfig.getMatchUrl("2276353", bookmakerMatchService.createFromMatch(match, BookmakerType.SBOBET).get())))
                .thenReturn(Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("sbobet_football_match_france_peru.html").toURI())))));
    }

    @Test
    public void test() {
        CompletableFuture<Void> future = engineService.handle(matches);
        future.join();
    }
}