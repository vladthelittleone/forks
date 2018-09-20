package com.savik.model;

import com.savik.domain.BookmakerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Bet {
    BookmakerType bookmakerType;

    BookmakerCoeff bookmakerCoeff;

    BookmakerMatchWrapper wrapper;

    public Bet(BookmakerType bookmakerType, BookmakerCoeff bookmakerCoeff) {
        this.bookmakerType = bookmakerType;
        this.bookmakerCoeff = bookmakerCoeff;
    }

    @Override
    public String toString() {
        return "{" + bookmakerType + "," +
                wrapper.getDefaultLogString() + ", bC={" + bookmakerCoeff + "}}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bet bet = (Bet) o;

        if (bookmakerType != bet.bookmakerType) return false;
        return bookmakerCoeff.equals(bet.bookmakerCoeff);
    }

    @Override
    public int hashCode() {
        return bookmakerCoeff.hashCode();
    }
}
