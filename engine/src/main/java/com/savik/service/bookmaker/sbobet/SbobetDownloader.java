package com.savik.service.bookmaker.sbobet;

import com.savik.domain.SportType;
import com.savik.http.HttpClient;
import com.savik.service.bookmaker.BookmakerMatch;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SbobetDownloader {

    @Autowired
    HttpClient httpClient;

    @Autowired
    SbobetConfig sbobetConfig;

    public Document download(String sbobetMatchId, BookmakerMatch bookmakerMatch) {
        String resultUrl = sbobetConfig.getMatchUrl(sbobetMatchId, bookmakerMatch);
        return httpClient.downloadAntibot(resultUrl);
    }

    public Document download(SportType sportType, int daysFromToday) {
        String resultUrl = sbobetConfig.getSportUrl(sportType, daysFromToday);
        return httpClient.downloadAntibot(resultUrl);
    }

}
