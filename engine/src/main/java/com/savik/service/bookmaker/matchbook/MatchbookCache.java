package com.savik.service.bookmaker.matchbook;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.domain.SportType;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
class MatchbookCache {

    private Map<SportType,Set<BookmakerMatchResponse>> cache = new HashMap<>();

    public void addAll(SportType type, Collection<BookmakerMatchResponse> matches) {
        cache.putIfAbsent(type, ConcurrentHashMap.newKeySet());
        cache.get(type).addAll(matches);
    }
    
    public boolean hasSportEvents(SportType sport) {
        return cache.containsKey(sport);
    }

    public Optional<BookmakerMatchResponse> find(BookmakerMatchWrapper bookmakerMatchWrapper) {
        BookmakerLeague bookmakerLeague = bookmakerMatchWrapper.getBookmakerLeague();
        BookmakerTeam homeTeam = bookmakerMatchWrapper.getHomeTeam();
        BookmakerTeam guestTeam = bookmakerMatchWrapper.getAwayTeam();
        final Set<BookmakerMatchResponse> matches = cache.get(bookmakerMatchWrapper.getMatch().getSportType());
        for (BookmakerMatchResponse cachedMatch : matches) {
            if (bookmakerLeague.getBookmakerId().equalsIgnoreCase(cachedMatch.getBookmakerLeagueId()) &&
                    homeTeam.getName().equalsIgnoreCase(cachedMatch.getBookmakerHomeTeamName()) &&
                    guestTeam.getName().equalsIgnoreCase(cachedMatch.getBookmakerAwayTeamName())) {
                log.debug("Match info was found in cache: " + bookmakerMatchWrapper.getDefaultLogString());
                return Optional.of(cachedMatch);
            }
        }
        log.info(String.format("Match info wasn't found in cache: days = %s, %s, %s",
                bookmakerMatchWrapper.getDaysFromToday(),
                bookmakerMatchWrapper.getMatch().getDate(),
                bookmakerMatchWrapper.getDefaultLogString()));
        return Optional.empty();
    }
}
