package com.savik.service.bookmaker.sbobet;

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
public class SbobetCache {

    private Set<BookmakerMatchResponse> cache = new HashSet<>();

    public void addAll(Collection<BookmakerMatchResponse> matches) {
        cache.addAll(matches);
    }

    public void add(BookmakerMatchResponse match) {
        cache.add(match);
    }

    public Optional<BookmakerMatchResponse> find(BookmakerMatch bookmakerMatch) {
        // todo concurrent
        BookmakerLeague bookmakerLeague = bookmakerMatch.getBookmakerLeague();
        BookmakerTeam homeTeam = bookmakerMatch.getHomeTeam();
        BookmakerTeam guestTeam = bookmakerMatch.getGuestTeam();
        for (BookmakerMatchResponse cachedMatch : cache) {
            if (Objects.equals(bookmakerLeague.getBookmakerId(), cachedMatch.getBookmakerLeagueId()) &&
                    Objects.equals(homeTeam.getName(), cachedMatch.getBookmakerHomeTeamName()) &&
                    Objects.equals(guestTeam.getName(), cachedMatch.getBookmakerAwayTeamName())) {
                log.debug("Match info was found in cache: " + cachedMatch);
                return Optional.of(cachedMatch);
            }
        }
        log.debug("Match info wasn't found in cache");
        return Optional.empty();
    }
}
