package com.savik.flashscore;

import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        matches = matches.stream().filter(m -> m.getFlashscoreLeagueId().equals("lrMHUHDc") &&
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
        System.out.println("return matches;");

    }
}
