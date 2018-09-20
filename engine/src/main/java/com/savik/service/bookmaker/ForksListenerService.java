package com.savik.service.bookmaker;

import com.savik.domain.Match;
import com.savik.events.ForkFoundEvent;
import com.savik.model.Bet;
import com.savik.utils.BookmakerUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class ForksListenerService {
    
    @EventListener
    public void handle(final ForkFoundEvent event) {
        final Match match = event.getMatch();
        final Bet first = event.getFirst();
        final Bet second = event.getSecond();
        StringBuilder builder = new StringBuilder("\n\nFork was found: ");
        builder.append("percentage=").append(BookmakerUtils.formatFork(first.getBookmakerCoeff(), second.getBookmakerCoeff()))
                .append(" id = ").append(match.getFlashscoreId())
                .append(" hT = ").append(match.getHomeTeam().getName()).append(" aT = ").append(match.getAwayTeam().getName())
                .append("\n b1=").append(first).append("\n b2=").append(second).append("\n");
        log.info(builder.toString());
    }

}

