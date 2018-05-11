package com.savik.flashscore;

import com.savik.flashscore.exception.ParseException;
import lombok.experimental.UtilityClass;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@UtilityClass
public class FlashscoreUtils {

    public static String getHomeTeamId(Document matchHtml) {
        return getTeamId(matchHtml, 0);
    }
    public static String getGuestTeamId(Document matchHtml) {
        return getTeamId(matchHtml, 1);
    }

    private static String getTeamId(Document matchHtml, int teamIndex) {
        Elements teams = matchHtml.getElementsByClass("tomyteams");
        String htmlElementId = teams.get(teamIndex).attr("id");
        String teamId = htmlElementId.substring(htmlElementId.length() - 8);
        if(!teamId.matches("[a-zA-Z0-9]{8}")) {
            throw new ParseException("team id is invalid: " + teamId);
        }
        return teamId;
    }
}
