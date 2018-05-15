package com.savik.flashscore;

import com.savik.domain.Match;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class Parser {

    @Autowired
    Downloader downloader;

    @Autowired
    FlashscoreResponseParser flashscoreResponseParser;

    @Autowired
    FlashscoreResponseProcessor flashscoreResponseProcessor;

    public void parse(List<SportConfig> sportConfigs, int day) {
        for (SportConfig sportConfig : sportConfigs) {
            parseConfig(sportConfig, day);
        }
    }

    private void parseConfig(SportConfig sportConfig, int day) {
        log.debug("Start parse sportConfig - " + sportConfig);

        Document document = downloader.downloadSportMatchesSchedule(sportConfig, day);
        List<Match> matches = flashscoreResponseParser.parse(document);
        flashscoreResponseProcessor.process(sportConfig, matches);

        log.debug("Finished parse sportConfig");
    }
}
