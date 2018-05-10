package com.savik.flashscore;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class Parser {

    public void parse(List<SportConfig> sportConfigs) {
        for (SportConfig sportConfig : sportConfigs) {
            parseConfig(sportConfig);
        }
    }

    private void parseConfig(SportConfig sportConfig) {
        log.debug("Start parse sportConfig - " + sportConfig);
        log.debug("Finished parse sportConfig");
    }
}
