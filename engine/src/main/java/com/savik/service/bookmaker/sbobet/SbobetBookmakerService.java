package com.savik.service.bookmaker.sbobet;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.domain.SportType;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerService;
import com.savik.service.bookmaker.Downloader;
import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SbobetBookmakerService extends BookmakerService {

    @Autowired
    Downloader downloader;

    @Override
    protected BookmakerType getBookmakerType() {
        return BookmakerType.SBOBET;
    }

    @Override
    public void handle(BookmakerMatch bookmakerMatch) {

        Match match = bookmakerMatch.getMatch();
        SportType sportType = match.getSportType();

        if (sportType == SportType.FOOTBALL) {
            Document download = downloader.download("https://www.sbobet.com/euro/football");
            Element script = download.getElementsByTag("script").last();
            String scriptText = script.childNodes().get(0).toString();

            String jsArray = scriptText.substring(scriptText.indexOf("onUpdate('od',") + 14, scriptText.indexOf("); }"));
            JSONArray jsonObject = new JSONArray(jsArray);

            String a = "";
        }

    }
}
