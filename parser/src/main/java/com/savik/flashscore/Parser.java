package com.savik.flashscore;

import com.savik.domain.BookmakerType;
import com.savik.domain.FlashscoreLeagues;
import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class Parser {

    @Autowired
    ParserDownloader downloader;

    @Autowired
    FlashscoreResponseParser flashscoreResponseParser;

    @Autowired
    FlashscoreResponseProcessor flashscoreResponseProcessor;

    public void parse(List<SportConfig> sportConfigs, int day) {
        for (SportConfig sportConfig : sportConfigs) {
            parseConfig(sportConfig, day);
        }
    }

    private void parseConfig(SportConfig sportConfig, int day) {
        log.debug("Start parse sportConfig - " + sportConfig);

        Document document = downloader.downloadSportMatchesSchedule(sportConfig, day);
        List<Match> matches = flashscoreResponseParser.parse(document);
        matches = matches.stream().filter(m -> m.getFlashscoreLeagueId().equals("QVmLl54o") &&
                m.getMatchStatus() == MatchStatus.PREMATCH).collect(Collectors.toList());
        flashscoreResponseProcessor.process(sportConfig, matches);
        temp(matches);

        log.debug("Finished parse sportConfig");
    }

    private void temp(List<Match> matches) {
        System.out.println("List<Match> matches = new ArrayList();");
        for (Match match : matches) {
            System.out.println("matches.add(");
            System.out.println("Match.builder()");
            System.out.println(".flashscoreId(\"" + match.getFlashscoreId() + "\")");
            System.out.println(".flashscoreLeagueId(\"" + match.getFlashscoreLeagueId() + "\")");
            System.out.println(".sportType(" + match.getSportType() + ")");
            System.out.println(".date(LocalDateTime.parse(\"" + match.getDate().toString() + "\"))");
            System.out.println(".matchStatus(" + match.getMatchStatus() + ")");
            System.out.println(".homeTeam(Team.builder().name(\"" + match.getHomeTeam().getName() + "\").flashscoreId(\"" + match.getHomeTeam().getFlashscoreId() + "\").sportType(" + match.getSportType() + ").build())");
            System.out.println(".awayTeam(Team.builder().name(\"" + match.getAwayTeam().getName() + "\").flashscoreId(\"" + match.getAwayTeam().getFlashscoreId() + "\").sportType(" + match.getSportType() + ").build())");
            System.out.println(".build()");
            System.out.println(");");
        }
        final List<BookmakerType> types = Arrays.asList(BookmakerType.values());
        for (BookmakerType type : types) {
            Set<String> ids = new HashSet<>();
            System.out.println("\n\n\n\n");
            for (Match match : matches) {
                if (!ids.contains(match.getHomeTeam().getFlashscoreId())) {
                    ids.add(match.getHomeTeam().getFlashscoreId());
                    System.out.println("INSERT INTO PUBLIC.BOOKMAKER_TEAM(BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME) VALUES('" + type + "', '" + match.getHomeTeam().getFlashscoreId() + "', '', '" + match.getHomeTeam().getName() + "');");
                }
                if (!ids.contains(match.getAwayTeam().getFlashscoreId())) {
                    ids.add(match.getAwayTeam().getFlashscoreId());
                    System.out.println("INSERT INTO PUBLIC.BOOKMAKER_TEAM(BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME) VALUES('" + type + "', '" + match.getAwayTeam().getFlashscoreId() + "', '', '" + match.getAwayTeam().getName() + "');");
                }
            }
        }

        System.out.println("return matches;");

    }
}
