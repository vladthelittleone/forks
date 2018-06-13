package com.savik.service.bookmaker;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerPK;
import com.savik.domain.BookmakerTeam;
import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.domain.Team;
import com.savik.repository.BookmakerLeagueRepository;
import com.savik.repository.BookmakerTeamRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Log4j2
public class BookmakerMatchService {

    @Autowired
    BookmakerTeamRepository bookmakerTeamRepository;

    @Autowired
    BookmakerLeagueRepository bookmakerLeagueRepository;

    public Optional<BookmakerMatch> createFromMatch(Match match, BookmakerType bookmakerType) {
        @NotNull Team homeTeam = match.getHomeTeam();
        @NotNull Team awayTeam = match.getAwayTeam();
        boolean somethingWasntFound = false;
        Optional<BookmakerTeam> dbHomeTeam = bookmakerTeamRepository.findById(new BookmakerPK(homeTeam.getFlashscoreId(), bookmakerType));
        if (!dbHomeTeam.isPresent()) {
            log.warn(format("Home team wan't found in db. Team id: %s, name: %s, bookType: %s",
                    homeTeam.getFlashscoreId(), homeTeam.getName(), bookmakerType));
            somethingWasntFound = true;
        }
        Optional<BookmakerTeam> dbAwayTeam = bookmakerTeamRepository.findById(new BookmakerPK(awayTeam.getFlashscoreId(), bookmakerType));
        if (!dbAwayTeam.isPresent()) {
            log.warn(format("Away team wan't found in db. Team id: %s, name: %s, bookType: %s",
                    awayTeam.getFlashscoreId(), awayTeam.getName(), bookmakerType));
            somethingWasntFound = true;
        }
        Optional<BookmakerLeague> league = bookmakerLeagueRepository.findById(new BookmakerPK(match.getFlashscoreLeagueId(), bookmakerType));
        if (!league.isPresent()) {
            log.warn(format("League wan't found in db. League id: %s, bookType: %s", match.getFlashscoreLeagueId(), bookmakerType));
            somethingWasntFound = true;
        }
        if (somethingWasntFound) {
            return Optional.empty();
        }

        return Optional.of(
                BookmakerMatch.builder()
                        .match(match)
                        .homeTeam(dbHomeTeam.get())
                        .awayTeam(dbAwayTeam.get())
                        .bookmakerLeague(league.get())
                        .build()
        );
    }
}
