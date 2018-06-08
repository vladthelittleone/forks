package com.savik.service.bookmaker;

import com.savik.domain.BookmakerPK;
import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.repository.BookmakerLeagueRepository;
import com.savik.repository.BookmakerTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmakerMatchService {

    @Autowired
    BookmakerTeamRepository bookmakerTeamRepository;

    @Autowired
    BookmakerLeagueRepository bookmakerLeagueRepository;

    public BookmakerMatch createFromMatch(Match match, BookmakerType bookmakerType) {
        return
                BookmakerMatch.builder()
                        .match(match)
                        .homeTeam(bookmakerTeamRepository.findById(new BookmakerPK(match.getHomeTeam().getFlashscoreId(), bookmakerType)).get())
                        .guestTeam(bookmakerTeamRepository.findById(new BookmakerPK(match.getAwayTeam().getFlashscoreId(), bookmakerType)).get())
                        .bookmakerLeague(bookmakerLeagueRepository.findById(new BookmakerPK(match.getFlashscoreLeagueId(), bookmakerType)).get())
                        .build();
    }
}
