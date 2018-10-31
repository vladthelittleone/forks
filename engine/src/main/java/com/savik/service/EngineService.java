package com.savik.service;

import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerAggregationService;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class EngineService {

    BookmakerAggregationService bookmakerAggregationService;

    public EngineService(BookmakerAggregationService bookmakerAggregationService) {
        this.bookmakerAggregationService = bookmakerAggregationService;
    }

    public CompletableFuture<Void> handle(List<Match> matches) {
        log.info("Start handling matches, size = " + matches.size());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Match match : matches) {
            log.debug("handling match: " + match);
            CompletableFuture<Void> handle = bookmakerAggregationService.handle(match);
            futures.add(handle);
            log.debug("match was handled: " + match);
        }
        log.info("Matches were handled successfully, size = " + matches.size());
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
    }
}
