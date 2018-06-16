package com.savik.service.bookmaker.sbobet;

import com.github.alessiop86.antiantibotcloudflare.AntiAntiBotCloudFlare;
import com.github.alessiop86.antiantibotcloudflare.OkHttpAntiAntibotCloudFlareFactory;
import com.github.alessiop86.antiantibotcloudflare.exceptions.AntiAntibotException;
import com.savik.domain.SportType;
import com.savik.http.Downloader;
import com.savik.http.exception.DownloadException;
import com.savik.service.bookmaker.BookmakerMatch;
import org.jsoup.Jsoup;
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
        return download(resultUrl);
    }

    public Document download(SportType sportType, int daysFromToday) {
        String resultUrl = sbobetConfig.getSportUrl(sportType, daysFromToday);
        return download(resultUrl);
    }

    public Document download(String url) {
        try {
            AntiAntiBotCloudFlare antibot = new OkHttpAntiAntibotCloudFlareFactory().createInstanceNonAndroid();
            String html = antibot.getUrl(url);
            return Jsoup.parse(html);
        } catch (AntiAntibotException e) {
            throw new DownloadException(e);
        }
    }

}
