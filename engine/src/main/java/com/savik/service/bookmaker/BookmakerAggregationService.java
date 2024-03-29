package com.savik.service.bookmaker;

import com.savik.aop.MatchIdLogging;
import com.savik.domain.Match;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Log4j2
public class BookmakerAggregationService {

    List<BookmakerService> bookmakerServices;

    public BookmakerAggregationService(List<BookmakerService> bookmakerServices) {
        if (bookmakerServices.isEmpty()) {
            log.info("bookmaker services should not be empty");
            throw new IllegalArgumentException();
        }
        log.debug("Aggregation bookmaker service was initiate by bookmaker services:"
                + bookmakerServices.stream().map(BookmakerService::getBookmakerType).collect(toList()));
        this.bookmakerServices = bookmakerServices;
    }

    @Async("slowBookmakersExecutor")
    public CompletableFuture<Void> slowHandle(Match match) {
        return handle(match);
    }

    @Async("fastBookmakersExecutor")
    public CompletableFuture<Void> fastHandle(Match match) {
        return handle(match);
    }

    @MatchIdLogging
    private CompletableFuture<Void> handle(Match match) {
        List<CompletableFuture<Optional<BookmakerMatchResponse>>> futures = new ArrayList<>();
        for (BookmakerService bookmakerService : bookmakerServices) {
            log.info(String.format("Start handling match. matchId: %s, bookmaker service: %s, ", match.getFlashscoreId(), bookmakerService.getBookmakerType()));
            CompletableFuture<Optional<BookmakerMatchResponse>> future = bookmakerService.handle(match);
            futures.add(future);
            log.info(String.format("Match was handled. matchId: %s, bookmaker service: %s", match.getFlashscoreId(), bookmakerService.getBookmakerType()));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
    }
}
