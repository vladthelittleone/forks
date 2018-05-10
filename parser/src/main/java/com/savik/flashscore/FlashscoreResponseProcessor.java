package com.savik.flashscore;

import com.savik.FutureMatch;
import com.savik.repository.FutureMatchRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FlashscoreResponseProcessor {

    @Autowired
    FutureMatchRepository futureMatchRepository;


    public void process(List<FutureMatch> futureMatches) {
        for (FutureMatch futureMatch : futureMatches) {
            log.debug("Start process future match - " + futureMatch.getFlashscoreId());

            List<FutureMatch> dbFutureMatches = futureMatchRepository.findAllById(futureMatches.stream()
                    .map(FutureMatch::getFlashscoreId).collect(Collectors.toList()));

            Optional<FutureMatch> dbMatch = dbFutureMatches.stream()
                    .filter(dbM -> dbM.getFlashscoreId().equals(futureMatch.getFlashscoreId())).findFirst();

            if(dbMatch.isPresent()) {
                log.debug("Match was found in db - " + dbMatch.get());
            } else {
                log.debug("Match wasn't found in db");
            }

            log.debug("Finished process future match - " + futureMatch.getFlashscoreId());
        }
    }

}
