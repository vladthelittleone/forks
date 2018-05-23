package com.savik.service.bookmaker;

import com.savik.domain.BookmakerTeamPK;
import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.repository.BookmakerTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmakerMatchService {

    @Autowired
    BookmakerTeamRepository bookmakerTeamRepository;

    public BookmakerMatch createFromMatch(Match match, BookmakerType bookmakerType) {
        return
                BookmakerMatch.builder()
                        .match(match)
                        .homeTeam(bookmakerTeamRepository.findById(new BookmakerTeamPK(match.getHomeTeam().getFlashscoreId(), bookmakerType)).get())
                        .guestTeam(bookmakerTeamRepository.findById(new BookmakerTeamPK(match.getGuestTeam().getFlashscoreId(), bookmakerType)).get())
                        .build();
    }
}
