package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Builder
public class BookmakerMatchResponse {

    BookmakerType bookmakerType;

    String bookmakerMatchId;

    String bookmakerLeagueId;

    String bookmakerHomeTeamName;

    String bookmakerGuestTeamName;

    List<BookmakerCoeff> bookmakerCoeffs;

    @Override
    public String toString() {
        return "{" +
                "b matchId='" + bookmakerMatchId + '\'' +
                ", b leagueId='" + bookmakerLeagueId + '\'' +
                ", home='" + bookmakerHomeTeamName + '\'' +
                ", guest='" + bookmakerGuestTeamName + '\'' +
                ", coeffs=" + bookmakerCoeffs +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmakerMatchResponse that = (BookmakerMatchResponse) o;
        return Objects.equals(bookmakerMatchId, that.bookmakerMatchId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bookmakerMatchId);
    }
}
