package com.savik.service.bookmaker;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerPK;
import com.savik.domain.BookmakerTeam;
import com.savik.domain.BookmakerType;
import com.savik.domain.FlashscoreLeagues;
import com.savik.domain.Match;
import com.savik.domain.Team;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.repository.BookmakerLeagueRepository;
import com.savik.repository.BookmakerTeamRepository;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@Service
@Log4j2
public class BookmakerMatchService {

    @Autowired
    BookmakerTeamRepository bookmakerTeamRepository;

    @Autowired
    BookmakerLeagueRepository bookmakerLeagueRepository;
    
    Set<TeamEntry> teams = ConcurrentHashMap.newKeySet();
    Set<LeagueEntry> leagues = ConcurrentHashMap.newKeySet();
    
    public Optional<BookmakerMatchWrapper> createFromMatch(Match match, BookmakerType bookmakerType) {
        @NotNull Team homeTeam = match.getHomeTeam();
        @NotNull Team awayTeam = match.getAwayTeam();
        boolean somethingWasntFound = false;
        Optional<BookmakerTeam> dbHomeTeam = bookmakerTeamRepository.findById(new BookmakerPK(homeTeam.getFlashscoreId(), bookmakerType));
        final TeamEntry homeTeamEntry = new TeamEntry(homeTeam.getFlashscoreId(), homeTeam.getName(), bookmakerType);
        if (!dbHomeTeam.isPresent() && !teams.contains(homeTeamEntry)) {
            log.warn(format("Home team wan't found in db. Team id: %s, name: %s, bookType: %s",
                    homeTeam.getFlashscoreId(), homeTeam.getName(), bookmakerType));
            somethingWasntFound = true;
            teams.add(homeTeamEntry);
        }
        Optional<BookmakerTeam> dbAwayTeam = bookmakerTeamRepository.findById(new BookmakerPK(awayTeam.getFlashscoreId(), bookmakerType));
        final TeamEntry awayTeamEntry = new TeamEntry(homeTeam.getFlashscoreId(), homeTeam.getName(), bookmakerType);
        if (!dbAwayTeam.isPresent() && !teams.contains(awayTeamEntry)) {
            log.warn(format("Away team wan't found in db. Team id: %s, name: %s, bookType: %s",
                    awayTeam.getFlashscoreId(), awayTeam.getName(), bookmakerType));
            somethingWasntFound = true;
            teams.add(awayTeamEntry);
        }
        Optional<BookmakerLeague> league = bookmakerLeagueRepository.findById(new BookmakerPK(match.getFlashscoreLeagueId(), bookmakerType));
        final LeagueEntry leagueEntry = new LeagueEntry(match.getFlashscoreLeagueId(), bookmakerType);
        if (!league.isPresent() && !leagues.contains(leagueEntry)) {
            log.warn(format("League wan't found in db. League id: %s, bookType: %s, %s", match.getFlashscoreLeagueId(), bookmakerType, FlashscoreLeagues.FOOTBALL.getById(match.getFlashscoreLeagueId())));
            somethingWasntFound = true;
            leagues.add(leagueEntry);
        }
        if (somethingWasntFound) {
            return Optional.empty();
        }

        return Optional.of(
                BookmakerMatchWrapper.builder()
                        .match(match)
                        .homeTeam(dbHomeTeam.get())
                        .awayTeam(dbAwayTeam.get())
                        .bookmakerLeague(league.get())
                        .build()
        );
    }
}

@EqualsAndHashCode
@AllArgsConstructor
class TeamEntry {
    String id;
    String name;
    BookmakerType bookmakerType;
}

@EqualsAndHashCode
@AllArgsConstructor
class LeagueEntry {
    String id;
    BookmakerType bookmakerType;
}
