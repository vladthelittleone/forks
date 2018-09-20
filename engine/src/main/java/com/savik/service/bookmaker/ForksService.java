package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.events.ForkFoundEvent;
import com.savik.model.Bet;
import com.savik.model.BookmakerCoeff;
import com.savik.utils.BookmakerUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ForksService {

    @Autowired
    List<BookmakerService> bookmakerServices;

    @Autowired
    BookmakerEventPublisher bookmakerEventPublisher;

    @Async(value = "arbPrechecker")
    public void verifyExistence(Match match, List<ForkFoundEvent> forks) {
        final Set<BookmakerType> bookmakers = forks.stream()
                .map(e -> Arrays.asList(e.getFirst().getBookmakerType(), e.getSecond().getBookmakerType()))
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        final Map<BookmakerType, List<BookmakerCoeff>> coeffsByType = bookmakers.stream()
                .collect(Collectors.toMap(x -> x, bookmakerType -> getUpdatedCoeffs(bookmakerType, match)));

        for (ForkFoundEvent event : forks) {
            if (forkStillExist(coeffsByType, event)) {
                bookmakerEventPublisher.publishForkFoundEvent(event);
            }
        }

    }

    private boolean forkStillExist(Map<BookmakerType, List<BookmakerCoeff>> coeffsByType, ForkFoundEvent event) {
        final Bet first = event.getFirst();
        final Bet second = event.getSecond();
        final Optional<BookmakerCoeff> optionalFirstUpdatedBet =
                findCoeffInList(first.getBookmakerCoeff(), coeffsByType.get(first.getBookmakerType()));
        final Optional<BookmakerCoeff> optionalSecondUpdatedBet =
                findCoeffInList(second.getBookmakerCoeff(), coeffsByType.get(second.getBookmakerType()));
        if (!optionalFirstUpdatedBet.isPresent() || !optionalSecondUpdatedBet.isPresent()) {
            log.info("There is no updated bet");
            return false;
        }
        StringBuilder builder = new StringBuilder("\n\n Checking fork for availability: ");
        builder.append(" match ").append(event.getMatch().getDefaultLogString())
                .append(" old fork ").append(BookmakerUtils.formatFork(first.getBookmakerCoeff(), second.getBookmakerCoeff()))
                .append(" updated fork ").append(BookmakerUtils.formatFork(optionalFirstUpdatedBet.get(), optionalSecondUpdatedBet.get()))
                .append("\n\n");
        log.info(builder.toString());
        return optionalFirstUpdatedBet.get().isFork(optionalSecondUpdatedBet.get());
    }

    private Optional<BookmakerCoeff> findCoeffInList(BookmakerCoeff bookmakerCoeff, List<BookmakerCoeff> bookmakerCoeffs) {
        return bookmakerCoeffs.stream().filter(b -> b.isSame(bookmakerCoeff)).findFirst();
    }

    private List<BookmakerCoeff> getUpdatedCoeffs(BookmakerType bookmakerType, Match match) {
        final BookmakerService byBookType = getBookmakerServiceByType(bookmakerType);
        final CompletableFuture<Optional<BookmakerMatchResponse>> future = byBookType.handleWithoutPublishing(match);
        final Optional<BookmakerMatchResponse> optional = future.join();
        final BookmakerMatchResponse response = optional.orElseThrow(() -> new IllegalArgumentException("response should be not empty"));
        return response.getBookmakerCoeffs();
    }

    private BookmakerService getBookmakerServiceByType(BookmakerType bookmakerType) {
        return bookmakerServices.stream().filter(s -> s.getBookmakerType() == bookmakerType).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("invalid book type: " + bookmakerType));
    }
}
