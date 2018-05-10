package com.savik.flashscore;

import com.savik.FutureMatch;
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

    public void parse(List<SportConfig> sportConfigs) {
        for (SportConfig sportConfig : sportConfigs) {
            parseConfig(sportConfig);
        }
    }

    private void parseConfig(SportConfig sportConfig) {
        log.debug("Start parse sportConfig - " + sportConfig);

        Document document = downloader.downloadSportMatchesSchedule(sportConfig, 1);
        List<FutureMatch> parse = flashscoreResponseParser.parse(document);

        log.debug(parse);


        log.debug("Finished parse sportConfig");
    }
}
