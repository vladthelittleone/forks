package com.savik;

import com.savik.domain.Match;
import com.savik.domain.SportType;
import com.savik.domain.Team;
import com.savik.service.bookmaker.BookmakerAggregationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;



@ExtendWith(SpringExtension.class)
@IntegrationTest
public class ListenerIntegrationTest {

    @Autowired
    BookmakerAggregationService bookmakerAggregationService;

    @Test
    public void test() {
        bookmakerAggregationService.handle(
                Match.builder()
                        .homeTeam(Team.builder().flashscoreId("naHiWdnt").name("Austria").build())
                        .guestTeam(Team.builder().flashscoreId("hrgrswHh").name("Russia").build())
                        .flashscoreLeagueId("f1GbGBCd")
                        .sportType(SportType.FOOTBALL)
                        .date(LocalDateTime.now())
                        .build()
        );

    }
}