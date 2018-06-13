package com.savik.service;

import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerAggregationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class EngineService {

    @Autowired
    BookmakerAggregationService bookmakerAggregationService;

    public CompletableFuture<Void> handle(List<Match> matches) {
        log.info("Start handling matches, size = " + matches.size());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Match match : matches) {
            log.debug("handling match: " + match);
            futures.add(bookmakerAggregationService.handle(match));
            log.debug("match was handled: " + match);
        }
        log.info("Matches were handled successfully, size = " + matches.size());
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
    }
}
