package com.savik.service.bookmaker.matchbook;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import io.netty.util.internal.ConcurrentSet;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Component
@Log4j2
class MatchbookCache {

    private Set<BookmakerMatchResponse> cache = new ConcurrentSet<>();

    public void addAll(Collection<BookmakerMatchResponse> matches) {
        cache.addAll(matches);
    }

    public Optional<BookmakerMatchResponse> find(BookmakerMatchWrapper bookmakerMatchWrapper) {
        BookmakerLeague bookmakerLeague = bookmakerMatchWrapper.getBookmakerLeague();
        BookmakerTeam homeTeam = bookmakerMatchWrapper.getHomeTeam();
        BookmakerTeam guestTeam = bookmakerMatchWrapper.getAwayTeam();
        for (BookmakerMatchResponse cachedMatch : cache) {
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
