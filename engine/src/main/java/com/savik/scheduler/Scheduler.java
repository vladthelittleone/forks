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

@Component
@Log4j2
public class Scheduler {

    @Autowired
    ParserClient parserClient;

    @Autowired
    EngineService engineService;

    @Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 5_000)
    public void prematchesTask() {
        log.info("start scheduling task for today matches");
        log.info("Current Thread : {}", Thread.currentThread().getName());

        List<Match> matches = parserClient.getMatches(MatchFilter.builder().matchStatus(MatchStatus.PREMATCH).build());
        CompletableFuture<Void> future = engineService.handle(matches);
        future.join();

        log.info("matches were received: " + matches.size());

        log.info("finished scheduling task for today matches");
    }
}
