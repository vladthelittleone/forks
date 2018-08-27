package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.events.ForkFoundEvent;
import com.savik.model.Bet;
import com.savik.model.BookmakerCoeff;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.savik.model.BookmakerCoeff.getForkPercentage;

@Service
@Log4j2
public class ForksListenerService {

    @Autowired
    List<BookmakerService> bookmakerServices;

    DecimalFormat f = new DecimalFormat("##.000");

    //@Async
    @EventListener
    public void handle(final ForkFoundEvent event) {
        if(!forkIsStillExist(event)) {
            log.info("Fork was exist, but there isn't right now:" + event);
            return;
        }
        final Match match = event.getMatch();
        final Bet first = event.getFirst();
        final Bet second = event.getSecond();
        StringBuilder builder = new StringBuilder("Fork was found: ");
        builder.append("fork=" + formatFork(event)).append(match.getFlashscoreId()).append(" hT=").append(match.getHomeTeam().getName()).append(" aT=")
                .append(match.getAwayTeam().getName()).append(" b1=").append(first).append(" b2=").append(second);
        log.info(builder.toString());
    }

    private boolean forkIsStillExist(final ForkFoundEvent event) {
        final Bet first = event.getFirst();
        final Bet second = event.getSecond();
        final Match match = event.getMatch();
        final Optional<BookmakerCoeff> optionalFirstUpdatedBet = findUpdatedBet(first, match);
        final Optional<BookmakerCoeff> optionalsecondUpdatedBet = findUpdatedBet(second, match);
        if(!optionalFirstUpdatedBet.isPresent() || !optionalsecondUpdatedBet.isPresent()) {
            return false;
        }
        return optionalFirstUpdatedBet.get().isFork(optionalsecondUpdatedBet.get());
    }

    private Optional<BookmakerCoeff> findUpdatedBet(Bet bet, Match match) {
        final BookmakerService byBookType = getByBookType(bet.getBookmakerType());
        final CompletableFuture<Optional<BookmakerMatchResponse>> future = byBookType.handleWithoutPublishing(match);
        final Optional<BookmakerMatchResponse> optional = future.join();
        final BookmakerMatchResponse response = optional.orElseThrow(() -> new IllegalArgumentException("response should be not empty"));
        final List<BookmakerCoeff> bookmakerCoeffs = response.getBookmakerCoeffs();
        final BookmakerCoeff bookmakerCoeff = bet.getBookmakerCoeff();
        return bookmakerCoeffs.stream().filter(b -> b.isSameChain(bookmakerCoeff)).findFirst();
    }

    private BookmakerService getByBookType(BookmakerType bookmakerType) {
        return bookmakerServices.stream().filter(s -> s.getBookmakerType() == bookmakerType).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("invalid book type: " + bookmakerType));
    }

    public String formatFork(final ForkFoundEvent event) {
        final Bet first = event.getFirst();
        final Bet second = event.getSecond();
        return f.format(getForkPercentage(first.getBookmakerCoeff(), second.getBookmakerCoeff()));
    }
}
