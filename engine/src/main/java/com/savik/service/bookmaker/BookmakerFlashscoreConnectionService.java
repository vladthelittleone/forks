package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BookmakerFlashscoreConnectionService {

    public void printSuggestion(Match match, BookmakerType bookmakerType) {
        log.debug(String.format("INSERT INTO PUBLIC.BOOKMAKER_TEAM(BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME) VALUES('%s', '%s, '', '%s')",
                bookmakerType, match.getHomeTeam().getFlashscoreId(), match.getHomeTeam().getName()));
        log.debug(String.format("INSERT INTO PUBLIC.BOOKMAKER_TEAM(BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME) VALUES('%s', '%s, '', '%s')",
                bookmakerType, match.getAwayTeam().getFlashscoreId(), match.getAwayTeam().getName()));
    }
}