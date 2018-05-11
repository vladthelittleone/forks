package com.savik.flashscore;

import com.savik.flashscore.exception.DownloadException;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author Savushkin Yauheni
 * @since 13.04.2017
 */
@AllArgsConstructor
@Service
public class Downloader {

    @Autowired
    private DownloaderConfiguration configuration;

    public Document downloadSportMatchesSchedule(SportConfig sportConfig, Integer day) {
        return download(String.format(configuration.getSportMatches(), sportConfig.getFlashscoreSportId(), day));
    }

    public Document downloadMatchHtml(String flashscoreMatchId) {
        return download(String.format(configuration.getMatchUrl(), flashscoreMatchId));
    }


    public Document download(String url) {
        try {
            return Jsoup.connect(url).header("X-Fsign", configuration.getFsign()).get();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }

    public String getJson(String url) {
        try {
            return Jsoup.connect(url).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }

    public Document downloadFile(File file) {
        try {
            return Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }
}
