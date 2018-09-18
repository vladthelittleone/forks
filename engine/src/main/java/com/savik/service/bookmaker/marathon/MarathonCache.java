package com.savik.service.bookmaker.marathon;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.model.BookmakerMatchWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Log4j2
public class MarathonCache {

    private Set<BookmakerMatchResponse> cache = new HashSet<>();

    public synchronized void addAll(Collection<BookmakerMatchResponse> matches) {
        cache.addAll(matches);
    }

    public synchronized void add(BookmakerMatchResponse match) {
        cache.add(match);
    }
    
    public synchronized boolean isEmpty() {
        return cache.isEmpty();
    }

    public synchronized Optional<BookmakerMatchResponse> find(BookmakerMatchWrapper bookmakerMatchWrapper) {
        // todo concurrent
        BookmakerLeague bookmakerLeague = bookmakerMatchWrapper.getBookmakerLeague();
        BookmakerTeam homeTeam = bookmakerMatchWrapper.getHomeTeam();
        BookmakerTeam guestTeam = bookmakerMatchWrapper.getAwayTeam();
        for (BookmakerMatchResponse cachedMatch : cache) {
            if (bookmakerLeague.getName().equalsIgnoreCase(cachedMatch.getBookmakerLeagueId()) &&
                    homeTeam.getName().equalsIgnoreCase(cachedMatch.getBookmakerHomeTeamName()) &&
                    guestTeam.getName().equalsIgnoreCase(cachedMatch.getBookmakerAwayTeamName())) {
                log.debug("Match info was found in cache: " + bookmakerMatchWrapper.getDefaultLogString());
                return Optional.of(cachedMatch);
            }
        }
        log.info(String.format("Match info wasn't found in cache: %s", bookmakerMatchWrapper.getDefaultLogString()));
        return Optional.empty();
    }
}
