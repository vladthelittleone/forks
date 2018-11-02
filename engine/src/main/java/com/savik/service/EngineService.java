package com.savik.service;

import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerAggregationService;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Log4j2
public class EngineService {

    BookmakerAggregationService bookmakerAggregationService;

    public EngineService(BookmakerAggregationService bookmakerAggregationService) {
        this.bookmakerAggregationService = bookmakerAggregationService;
    }

    public CompletableFuture<Void> slowHandle(List<Match> matches) {
        return handle(matches, (Match m) -> bookmakerAggregationService.slowHandle(m));
    }

    public CompletableFuture<Void> fastHandle(List<Match> matches) {
        return handle(matches, (Match m) -> bookmakerAggregationService.fastHandle(m));
    }
    
    private CompletableFuture<Void> handle(List<Match> matches, Function<Match, CompletableFuture<Void>> matchHandler) {
        log.info("Start handling matches, size = " + matches.size());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Match match : matches) {
            log.debug("handling match: " + match);
            CompletableFuture<Void> handle = matchHandler.apply(match);
            futures.add(handle);
            log.debug("match was handled: " + match);
        }
        log.info("Matches were handled successfully, size = " + matches.size());
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
    }
}
