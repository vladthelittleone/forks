package com.savik.service.bookmaker.sbobet;

import com.savik.domain.SportType;
import com.savik.http.Downloader;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SbobetDownloader extends Downloader {

    @Autowired
    SbobetConfig sbobetConfig;

    public Document download(BookmakerMatchResponse bookmakerMatchResponse, BookmakerMatch bookmakerMatch) {
        String sbobetUrl = sbobetConfig.getUrl();
        String sportPrefix = sbobetConfig.getPrefixes().get(bookmakerMatch.getMatch().getSportType());
        String leagueName = bookmakerMatch.getBookmakerLeague().getName();
        String matchId = bookmakerMatchResponse.getBookmakerMatchId();
        String resultUrl = combineMatchUrl(sbobetUrl, sportPrefix, leagueName, matchId,
                bookmakerMatch.getHomeTeam().getName(), bookmakerMatch.getGuestTeam().getName());
        return download(resultUrl);
    }

    public Document download(SportType sportType, int daysFromToday) {
        String sbobetUrl = sbobetConfig.getUrl();
        String sportPrefix = sbobetConfig.getPrefixes().get(sportType);
        String resultUrl = combineSportUrl(sbobetUrl, sportPrefix, daysFromToday);
        return download(resultUrl);
    }

    private String combineSportUrl(String sbobetUrl, String sportPrefix, int daysFromToday) {
        String daysSbobetFormat = LocalDateTime.now().plusDays(daysFromToday).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (daysFromToday == 0) {
            daysSbobetFormat = "";
        }
        return sbobetUrl + sportPrefix + daysSbobetFormat;
    }

    private String combineMatchUrl(String sbobetUrl, String sportPrefix, String leagueName, String matchId,
                                   String homeName, String guestName) {

        String newHomeName = homeName.toLowerCase().replaceAll("\\s+","-");
        String newGuestName = guestName.toLowerCase().replaceAll("\\s+","-");
        return sbobetUrl + sportPrefix + "/" + leagueName + "/" + matchId + "/" + newHomeName + "-" + newGuestName;
    }

}
