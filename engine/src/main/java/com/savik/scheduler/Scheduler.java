package com.savik.scheduler;

import com.savik.client.ParserClient;
import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import com.savik.filter.MatchFilter;
import com.savik.service.EngineService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Component
@Log4j2
public class Scheduler {

    @Autowired
    ParserClient parserClient;

    @Autowired
    EngineService slowBooksEngineService;

    @Autowired
    EngineService fastBooksEngineService;

    @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 5_000)
    public void slowBookmakersTodayMatches() {
        handle(matches -> slowBooksEngineService.handle(matches));
    }

    @Scheduled(fixedDelay = 1, initialDelay = 5_000)
    public void fastBookmakersTodayMatches() {
        handle(matches -> fastBooksEngineService.handle(matches));
    }

    private void handle(Function<List<Match>, CompletableFuture<Void>> matchesHandler) {
        log.info("start scheduling task for today matches");
        log.info("Current Thread : {}", Thread.currentThread().getName());

        List<Match> matches = parserClient.getMatches(MatchFilter.builder().matchStatus(MatchStatus.PREMATCH).build());
        CompletableFuture<Void> future = matchesHandler.apply(matches);
        future.join();

        log.info("matches were received: " + matches.size());
        log.info("finished scheduling task for today matches");
    }
}
