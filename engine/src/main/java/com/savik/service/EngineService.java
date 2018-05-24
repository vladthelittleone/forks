package com.savik.service;

import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerAggregationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class EngineService {

    @Autowired
    BookmakerAggregationService bookmakerAggregationService;

    public void handle(List<Match> matches) {
        log.info("Start handling matches, size = " + matches.size());

        for (Match match : matches) {
            log.debug("handling match: " + match);
            bookmakerAggregationService.handle(match);
            log.debug("match was handled: " + match);
        }

        log.info("Matches were handled successfully, size = " + matches.size());


        bookmakerAggregationService.handle(null);



    }
}
