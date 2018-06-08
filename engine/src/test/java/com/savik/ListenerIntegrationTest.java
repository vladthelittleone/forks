package com.savik;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.domain.SportType;
import com.savik.domain.Team;
import com.savik.service.bookmaker.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerEventPublisher;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.ForkFoundEvent;
import com.savik.service.bookmaker.ForksListenerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@ExtendWith(SpringExtension.class)
@IntegrationTest
public class ListenerIntegrationTest {

    @Autowired
    BookmakerEventPublisher eventPublisher;

    @MockBean
    ForksListenerService forksListenerService;

    @Test
    public void test() {
        Match match = Match.builder()
                .homeTeam(Team.builder().flashscoreId("6LHwBDGU").name("Czech Republic").build())
                .awayTeam(Team.builder().flashscoreId("EBE2Xb3l").name("Nigeria").build())
                .flashscoreLeagueId("f1GbGBCd")
                .sportType(SportType.FOOTBALL)
                .date(LocalDateTime.now())
                .flashscoreId("6wouPfkp")
                .build();


        BookmakerMatchResponse sbobetResponse = BookmakerMatchResponse.builder()
                .bookmakerType(BookmakerType.SBOBET)
                .bookmakerHomeTeamName("Czech Republic (n)")
                .bookmakerAwayTeamName("Nigeria")
                .bookmakerLeagueId("10")
                .bookmakerMatchId("")
                .bookmakerCoeffs(
                        Arrays.asList(
                                BookmakerCoeff.of(0., 1.95, HANDICAP, HOME, MATCH),
                                BookmakerCoeff.of(0., 1.95, HANDICAP, AWAY, MATCH)
                        )
                )
                .build();


        BookmakerMatchResponse pinnacleResponse = BookmakerMatchResponse.builder()
                .bookmakerType(BookmakerType.PINNACLE)
                .bookmakerHomeTeamName("Czech Republic (n)")
                .bookmakerAwayTeamName("Nigeria")
                .bookmakerLeagueId("2117")
                .bookmakerMatchId("")
                .bookmakerCoeffs(
                        Arrays.asList(
                                BookmakerCoeff.of(0., 1.85, HANDICAP, AWAY, MATCH),
                                BookmakerCoeff.of(0., 2.06, HANDICAP, HOME, MATCH)
                        )
                )
                .build();

        eventPublisher.publishMatchResponse(sbobetResponse, match);
        eventPublisher.publishMatchResponse(pinnacleResponse, match);

        verify(forksListenerService, times(1)).handle(Mockito.any(ForkFoundEvent.class));
    }
}