package com.savik.service.bookmaker.pinnacle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savik.domain.SportType;
import com.savik.exception.ParseException;
import com.savik.http.HttpClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class PinnacleDownloader {

    @Autowired
    HttpClient httpClient;

    @Autowired
    PinnacleConfig pinnacleConfig;

    @Autowired
    ObjectMapper objectMapper;

    public FixtureResponse downloadFixtures(SportType sportType) {
        try {
            String url = pinnacleConfig.getFixtureUrl(sportType);
            String json = downloadJson(url);
            //String json = new String(Files.readAllBytes(Paths.get("C:\\projects\\forks\\engine\\src\\main\\resources\\config\\bookmakers\\pinnacle\\fixtures.json")));
            return objectMapper.readValue(json, FixtureResponse.class);
        } catch (IOException e) {
            throw new ParseException("Failed parse fixture", e);
        }
    }

    public OddsResponse downloadOdds(SportType sportType) {
        try {
            String url = pinnacleConfig.getOddsUrl(sportType);
            String json = downloadJson(url);
            //String json = new String(Files.readAllBytes(Paths.get("C:\\projects\\forks\\engine\\src\\main\\resources\\config\\bookmakers\\pinnacle\\odds.json")));
            return objectMapper.readValue(json, OddsResponse.class);
        } catch (IOException e) {
            throw new ParseException("Failed parse odds", e);
        }
    }

    public String downloadJson(String url) {
        Map<String, String> headers = new HashMap<String, String>() {
            {
                put("Authorization", "Basic " + pinnacleConfig.getBase64Authentications());
            }
        };
        return httpClient.getJson(url, headers);
    }


}
