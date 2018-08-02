package com.savik.service.bookmaker.marathon;

import com.savik.http.HttpClient;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MarathonDownloader {

    @Autowired
    HttpClient httpClient;

    @Autowired
    MarathonConfig marathonConfig;

    public Document download(String marathonMatchId) {
        return httpClient.post(marathonConfig.getUrl(), Collections.singletonMap("treeId", marathonMatchId));
    }

}