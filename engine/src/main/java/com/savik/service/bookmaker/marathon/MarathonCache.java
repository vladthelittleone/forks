package com.savik.service.bookmaker.marathon;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
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

    public synchronized Optional<BookmakerMatchResponse> find(BookmakerMatch bookmakerMatch) {
        // todo concurrent
        BookmakerLeague bookmakerLeague = bookmakerMatch.getBookmakerLeague();
        BookmakerTeam homeTeam = bookmakerMatch.getHomeTeam();
        BookmakerTeam guestTeam = bookmakerMatch.getAwayTeam();
        for (BookmakerMatchResponse cachedMatch : cache) {
            if (Objects.equals(bookmakerLeague.getName(), cachedMatch.getBookmakerLeagueId()) &&
                    Objects.equals(homeTeam.getName(), cachedMatch.getBookmakerHomeTeamName()) &&
                    Objects.equals(guestTeam.getName(), cachedMatch.getBookmakerAwayTeamName())) {
                log.debug(String.format("Match info was found in cache: %s", bookmakerMatch.getDefaultLogString()));
                return Optional.of(cachedMatch);
            }
        }
        log.debug(String.format("Match info wasn't found in cache: %s", bookmakerMatch.getDefaultLogString()));
        return Optional.empty();
    }
}
