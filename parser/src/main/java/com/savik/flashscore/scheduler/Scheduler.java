package com.savik.flashscore.scheduler;

import com.savik.flashscore.Parser;
import com.savik.flashscore.SportConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class Scheduler {

    @Autowired
    Parser parser;

    @Autowired
    List<SportConfig> sportConfigs;

    public static final int TODAY = 0;


    @Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 5_000)
    public void todayMatchesTask() {
        log.info("start scheduling task for today matches");
        log.info("Current Thread : {}", Thread.currentThread().getName());

        //parser.parse(sportConfigs, TODAY);
        //parser.parse(sportConfigs, 1);
        parser.parse(sportConfigs, 2);
        //parser.parse(sportConfigs, 3);

        log.info("finished scheduling task for today matches");
    }
}
