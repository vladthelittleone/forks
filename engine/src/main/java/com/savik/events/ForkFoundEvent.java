package com.savik.events;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerCoeff;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ForkFoundEvent {

    Match match;

    BookmakerType newBookmakerType;

    BookmakerCoeff newBookmakerCoeff;

    BookmakerType oldBookmakerType;

    BookmakerCoeff oldBookmakerCoeff;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForkFoundEvent that = (ForkFoundEvent) o;

        if (!match.equals(that.match)) return false;
        if (newBookmakerType != that.newBookmakerType) return false;
        if (!newBookmakerCoeff.equals(that.newBookmakerCoeff)) return false;
        if (oldBookmakerType != that.oldBookmakerType) return false;
        return oldBookmakerCoeff.equals(that.oldBookmakerCoeff);
    }

    @Override
    public int hashCode() {
        int result = match.hashCode();
        result = 31 * result + newBookmakerType.hashCode();
        result = 31 * result + newBookmakerCoeff.hashCode();
        result = 31 * result + oldBookmakerType.hashCode();
        result = 31 * result + oldBookmakerCoeff.hashCode();
        return result;
    }
}
