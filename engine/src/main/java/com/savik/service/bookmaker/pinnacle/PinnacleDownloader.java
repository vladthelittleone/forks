package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.SportType;
import com.savik.http.Downloader;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PinnacleDownloader extends Downloader {

    @Autowired
    PinnacleConfig pinnacleConfig;

    public Document downloadFixtures(SportType sportType) {
        String url = pinnacleConfig.getFixtureUrl(sportType);
        return download(url);
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
