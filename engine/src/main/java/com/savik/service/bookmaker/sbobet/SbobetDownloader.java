package com.savik.service.bookmaker.sbobet;

import com.savik.domain.SportType;
import com.savik.http.Downloader;
import com.savik.service.bookmaker.BookmakerMatch;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SbobetDownloader {

    @Autowired
    Downloader downloader;

    @Autowired
    SbobetConfig sbobetConfig;

    public Document download(String sbobetMatchId, BookmakerMatch bookmakerMatch) {
        String resultUrl = sbobetConfig.getMatchUrl(sbobetMatchId, bookmakerMatch);
        return downloader.download(resultUrl);
    }

    public Document download(SportType sportType, int daysFromToday) {
        String resultUrl = sbobetConfig.getSportUrl(sportType, daysFromToday);
        return downloader.download(resultUrl);
    }

}
