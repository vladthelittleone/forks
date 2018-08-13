package com.savik.service.bookmaker;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerTeam;
import com.savik.domain.Match;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmakerMatch {

    Match match;

    BookmakerTeam homeTeam;

    BookmakerTeam awayTeam;

    BookmakerLeague bookmakerLeague;

    public int getDaysFromToday() {
        return match.getDaysFromToday();
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
