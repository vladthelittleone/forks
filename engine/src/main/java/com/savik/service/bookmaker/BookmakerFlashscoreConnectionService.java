package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.FlashscoreLeagues;
import com.savik.domain.Match;
import com.savik.domain.Team;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Log4j2
public class BookmakerFlashscoreConnectionService {
    
    Map<BookmakerType, Set<Team>> map = new HashMap<>();
    Map<BookmakerType, Set<FlashscoreLeagues.FOOTBALL>> leagues = new HashMap<>();

    public void printSuggestion(Match match, BookmakerType bookmakerType) {
        map.putIfAbsent(bookmakerType, new HashSet<>());
        leagues.putIfAbsent(bookmakerType, new HashSet<>());
        final Set<Team> teams = map.get(bookmakerType);
        final Set<FlashscoreLeagues.FOOTBALL> uniqueLeagues = leagues.get(bookmakerType);
        final FlashscoreLeagues.FOOTBALL byId = FlashscoreLeagues.FOOTBALL.getById(match.getFlashscoreLeagueId());
        if(!uniqueLeagues.contains(byId)) {
            log.debug(String.format("NO LEAGUE : %s - %s", bookmakerType, byId));
        }
        if(!teams.contains(match.getHomeTeam())) {
            log.debug(String.format("INSERT INTO PUBLIC.BOOKMAKER_TEAM(BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME) VALUES('%s', '%s', '', '%s');",
                    bookmakerType, match.getHomeTeam().getFlashscoreId(), match.getHomeTeam().getName())); 
        }
        if(!teams.contains(match.getAwayTeam())) {
            log.debug(String.format("INSERT INTO PUBLIC.BOOKMAKER_TEAM(BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME) VALUES('%s', '%s', '', '%s');",
                    bookmakerType, match.getAwayTeam().getFlashscoreId(), match.getAwayTeam().getName()));
        }
        teams.add(match.getHomeTeam());
        teams.add(match.getAwayTeam());
        uniqueLeagues.add(byId);
    }
}