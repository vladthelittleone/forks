package com.savik.service.bookmaker.matchbook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savik.domain.SportType;
import com.savik.http.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
class MatchbookDownloader {
    @Autowired
    HttpClient httpClient;

    @Autowired
    MatchbookConfig config;

    @Autowired
    ObjectMapper objectMapper;

    public MatchbookNavigationEntry getNavigation() {
        try {
            final String navigationUrl = config.getNavigationUrl();
            final String json = httpClient.getJson(navigationUrl);
            List<MatchbookNavigationEntry> parsedNavigationEntries = objectMapper.readValue(json, new TypeReference<List<MatchbookNavigationEntry>>() {
            });
            return parsedNavigationEntries.stream().filter(e -> "Sport".equalsIgnoreCase(e.getName())).findFirst().get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MatchbookEventsResponse getEvents(SportType sportType) {
        try {
            final String navigationUrl = config.getEventsUrl(sportType);
            String json = new String(Files.readAllBytes(Paths.get("/Users/savushkineo/Projects/java/gradle-test/forks/engine/src/main/resources/config/bookmakers/matchbook/events.json")));
            //final String json = httpClient.getPinnacleApacheJson(navigationUrl);
            final MatchbookEventsResponse matchbookEventsResponse = objectMapper.readValue(json, MatchbookEventsResponse.class);
            return matchbookEventsResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
