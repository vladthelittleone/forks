package com.savik.flashscore;

import com.savik.domain.BookmakerPK;
import com.savik.domain.BookmakerTeam;
import com.savik.domain.BookmakerType;
import com.savik.domain.FlashscoreLeagues;
import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import com.savik.repository.BookmakerTeamRepository;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

/*        List<String> leagues = Arrays.asList(SCOTLAND_1.getId(), RUSSIA_FNL.getId(), ENGLAND_1.getId(), ENGLAND_2.getId(), ENGLAND_CHAMPIONSHIP.getId(), ENGLAND_PREMIER.getId());
        matches = matches.stream().filter(m -> leagues.contains(m.getFlashscoreLeagueId()) &&
                m.getMatchStatus() == MatchStatus.PREMATCH).collect(Collectors.toList());*/
        final List<String> ids = Arrays.asList(FlashscoreLeagues.FOOTBALL.values()).stream()
                .map(v -> v.getId()).collect(Collectors.toList());
        matches = matches.stream().filter(m -> ids.contains(m.getFlashscoreLeagueId()) &&
                m.getMatchStatus() == MatchStatus.PREMATCH).collect(Collectors.toList());
        flashscoreResponseProcessor.process(sportConfig, matches);
        temp(matches);

        log.debug("Finished parse sportConfig");
    }

    @Autowired
    BookmakerTeamRepository teamRepository;
    
    
    private void temp(List<Match> matches) {
        Map<String, List<Match>> matchesByLeague = new HashMap<>();
        for (Match match : matches) {
            matchesByLeague.putIfAbsent(match.getFlashscoreLeagueId(), new ArrayList<>());
            matchesByLeague.get(match.getFlashscoreLeagueId()).add(match);
        }
        System.out.println("List<Match> matches = new ArrayList();");
        for (Map.Entry<String, List<Match>> entry : matchesByLeague.entrySet()) {
            final String leagueId = entry.getKey();
            System.out.println("/*  " + leagueId + ", " + FlashscoreLeagues.FOOTBALL.getById(leagueId) + " */");

            final List<Match> value = entry.getValue();
            for (Match match : value) {
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
        }
        System.out.println("return matches;");


        Set<String> ids = new HashSet<>();
        System.out.println("\n\n\n\n");

        for (Map.Entry<String, List<Match>> entry : matchesByLeague.entrySet()) {
            final String leagueId = entry.getKey();
            System.out.println("/*  " + leagueId + ", " + FlashscoreLeagues.FOOTBALL.getById(leagueId) + " */");
            final List<Match> value = entry.getValue();
            for (Match match : value) {
                final Optional<BookmakerTeam> homeTeam = teamRepository.findById(new BookmakerPK(match.getHomeTeam().getFlashscoreId(), BookmakerType.SBOBET));
                if (!homeTeam.isPresent() && !ids.contains(match.getHomeTeam().getFlashscoreId())) {
                    ids.add(match.getHomeTeam().getFlashscoreId());
                    System.out.println("INSERT INTO PUBLIC.BOOKMAKER_TEAM(BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME) VALUES('SBOBET', '" + match.getHomeTeam().getFlashscoreId() + "', '', '" + match.getHomeTeam().getName() + "');");
                }
                final Optional<BookmakerTeam> awayTeam = teamRepository.findById(new BookmakerPK(match.getAwayTeam().getFlashscoreId(), BookmakerType.SBOBET));
                if (!awayTeam .isPresent() && !ids.contains(match.getAwayTeam().getFlashscoreId())) {
                    ids.add(match.getAwayTeam().getFlashscoreId());
                    System.out.println("INSERT INTO PUBLIC.BOOKMAKER_TEAM(BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME) VALUES('SBOBET', '" + match.getAwayTeam().getFlashscoreId() + "', '', '" + match.getAwayTeam().getName() + "');");
                }
            }
        }


    }
}
