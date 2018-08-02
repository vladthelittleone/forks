package com.savik.service.bookmaker;

import com.savik.domain.Match;
import com.savik.events.ForkFoundEvent;
import com.savik.model.Bet;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

import static com.savik.model.BookmakerCoeff.getForkPercentage;

@Service
@Log4j2
public class ForksListenerService {

    DecimalFormat f = new DecimalFormat("##.00");
    @EventListener
    public void handle(final ForkFoundEvent event) {
        final Match match = event.getMatch();
        final Bet first = event.getFirst();
        final Bet second = event.getSecond();
        StringBuilder builder = new StringBuilder("Fork was found: ");
        builder.append("fork=" + calculateFork(event)).append(match.getFlashscoreId()).append(" hT=").append(match.getHomeTeam().getName()).append(" aT=")
                .append(match.getAwayTeam().getName()).append(" b1=").append(first).append(" b2=").append(second);
        log.info(builder.toString());
        
    }
    
    public String calculateFork(final ForkFoundEvent event) {
        final Bet first = event.getFirst();
        final Bet second = event.getSecond();
        return f.format(getForkPercentage(first.getBookmakerCoeff(), second.getBookmakerCoeff()));
    }
}
