package com.savik.service.bookmaker.pinnacle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savik.domain.SportType;
import com.savik.exception.ParseException;
import com.savik.http.Downloader;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class PinnacleDownloader extends Downloader {

    @Autowired
    PinnacleConfig pinnacleConfig;

    @Autowired
    ObjectMapper objectMapper;

    public FixtureResponse downloadFixtures(SportType sportType) {
        try {
            String url = pinnacleConfig.getFixtureUrl(sportType);
            Document json = download(url);
            return objectMapper.readValue(json.body().text(), FixtureResponse.class);
        } catch (IOException e) {
            throw new ParseException("Failed parse fixture", e);
        }
    }

    public OddsResponse downloadOdds(SportType sportType) {
        try {
            String url = pinnacleConfig.getOddsUrl(sportType);
            Document json = download(url);
            return objectMapper.readValue(json.body().text(), OddsResponse.class);
        } catch (IOException e) {
            throw new ParseException("Failed parse odds", e);
        }
    }

    @Override
    public Document download(String url) {
        Map<String, String> headers = new HashMap<String, String>() {
            {
                put("Authorization", "Basic " + pinnacleConfig.getBase64Authentications());
            }
        };
        return download(url, headers);
    }


}
