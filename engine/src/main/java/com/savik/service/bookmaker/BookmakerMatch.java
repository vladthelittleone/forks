package com.savik.service.bookmaker;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.domain.Match;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@Builder
public class BookmakerMatch {

    Match match;

    BookmakerTeam homeTeam;

    BookmakerTeam awayTeam;

    BookmakerLeague bookmakerLeague;

    public int getDaysFromToday() {
        LocalDateTime matchDate = match.getDate();
        int between = Period.between(LocalDate.now(), matchDate.toLocalDate()).getDays();
        return between;
    }

    @Override
    public String toString() {
        return "B.M.{" +
                "m=" + match +
                ", hT=" + homeTeam +
                ", gT=" + awayTeam +
                ", bL=" + bookmakerLeague +
                '}';
    }
}
