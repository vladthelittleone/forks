package com.savik.service.bookmaker;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.domain.Match;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
public class BookmakerMatch {

    Match match;

    BookmakerTeam homeTeam;

    BookmakerTeam guestTeam;

    BookmakerLeague bookmakerLeague;

    public int getDaysFromToday() {
        LocalDateTime matchDate = match.getDate();
        long between = ChronoUnit.DAYS.between(matchDate, LocalDateTime.now());
        return (int) between;
    }
}
