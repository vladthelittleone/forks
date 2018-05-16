package com.savik.flashscore;

import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import com.savik.domain.SportType;
import com.savik.domain.Team;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestUtils {

    public static Match createMatch() {
        return Match.builder()
                .date(LocalDateTime.now())
                .flashscoreId(UUID.randomUUID().toString())
                .flashscoreLeagueId(UUID.randomUUID().toString())
                .matchStatus(MatchStatus.FINISHED)
                .sportType(SportType.FOOTBALL)
                .homeTeam(createTeam())
                .guestTeam(createTeam())
                .build();
    }

    private static Team createTeam() {
        return Team.builder()
                .flashscoreId(UUID.randomUUID().toString())
                .name(UUID.randomUUID().toString())
                .sportType(SportType.FOOTBALL)
                .build();
    }
}
