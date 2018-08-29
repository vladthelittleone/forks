package com.savik.service.bookmaker.sbobet;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@Log4j2
public class SbobetCache {

    private Map<Integer, Set<BookmakerMatchResponse>> cache = new HashMap<>();

    public synchronized void addAll(Integer day, Collection<BookmakerMatchResponse> matches) {
        cache.putIfAbsent(day, new HashSet<>());
        cache.get(day).addAll(matches);
    }

    public synchronized void add(Integer day, BookmakerMatchResponse match) {
        cache.putIfAbsent(day, new HashSet<>());
        cache.get(day).add(match);
    }

    public synchronized boolean dayWasParsed(Integer day) {
        return !CollectionUtils.isEmpty(cache.get(day));
    }

    public synchronized Optional<BookmakerMatchResponse> find(BookmakerMatch bookmakerMatch) {
        // todo concurrent
        BookmakerLeague bookmakerLeague = bookmakerMatch.getBookmakerLeague();
        BookmakerTeam homeTeam = bookmakerMatch.getHomeTeam();
        BookmakerTeam guestTeam = bookmakerMatch.getAwayTeam();
        final Set<BookmakerMatchResponse> matches = cache.getOrDefault(bookmakerMatch.getDaysFromToday(), new HashSet<>());
        for (BookmakerMatchResponse cachedMatch : matches) {
            if (Objects.equals(bookmakerLeague.getBookmakerId(), cachedMatch.getBookmakerLeagueId()) &&
                    Objects.equals(homeTeam.getName(), cachedMatch.getBookmakerHomeTeamName()) &&
                    Objects.equals(guestTeam.getName(), cachedMatch.getBookmakerAwayTeamName())) {
                log.debug("Match info was found in cache: " + bookmakerMatch.getDefaultLogString());
                return Optional.of(cachedMatch);
            }
        }
        log.info("Match info wasn't found in cache:" + bookmakerMatch.getDefaultLogString());
        return Optional.empty();
    }
}
