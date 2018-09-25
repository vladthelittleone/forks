package com.savik.service.bookmaker.matchbook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savik.http.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MatchbookDownloader {
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
}
