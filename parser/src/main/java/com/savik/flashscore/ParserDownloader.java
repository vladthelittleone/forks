package com.savik.flashscore;

import com.savik.http.HttpClient;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Savushkin Yauheni
 * @since 13.04.2017
 */
@AllArgsConstructor
@Service
public class ParserDownloader {

    @Autowired
    HttpClient httpClient;

    @Autowired
    private DownloaderConfiguration configuration;

    public Document downloadSportMatchesSchedule(SportConfig sportConfig, Integer day) {
        return download(String.format(configuration.getSportMatches(), sportConfig.getFlashscoreSportId(), day));
    }

    public Document downloadMatchHtml(String flashscoreMatchId) {
        return download(String.format(configuration.getMatchUrl(), flashscoreMatchId));
    }


    public Document download(String url) {
        Map<String, String> headers = new HashMap<String, String>() {
            {
                put("X-Fsign", configuration.getFsign());
            }
        };
        return httpClient.download(url, headers);
    }
}
