package com.savik.service.bookmaker.sbobet;

import com.savik.domain.SportType;
import com.savik.http.Downloader;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SbobetDownloader extends Downloader {

    @Autowired
    SbobetConfig sbobetConfig;

    public Document download(SportType sportType, int daysFromToday) {
        String sbobetUrl = sbobetConfig.getUrl();
        String sportPrefix = sbobetConfig.getPrefixes().get(sportType);
        String resultUrl = combineUrl(sbobetUrl, sportPrefix, daysFromToday);
        return download(resultUrl);
    }

    private String combineUrl(String sbobetUrl, String sportPrefix, int daysFromToday) {
        String daysSbobetFormat = LocalDateTime.now().plusDays(daysFromToday).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(daysFromToday == 0) {
            daysSbobetFormat = "";
        }
        return sbobetUrl + sportPrefix + daysSbobetFormat;
    }

}
