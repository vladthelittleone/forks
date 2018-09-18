package com.savik.events;

import com.savik.domain.Match;
import com.savik.model.Bet;
import com.savik.model.BookmakerMatchWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ForkFoundEvent {

    BookmakerMatchWrapper wrapper;

    Bet first;

    Bet second;

    public Match getMatch() {
        return wrapper.getMatch();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForkFoundEvent that = (ForkFoundEvent) o;

        if (!wrapper.getMatch().equals(that.wrapper.getMatch())) return false;
        if (!first.equals(that.first) && !first.equals(that.second)) return false;
        return second.equals(that.second) || second.equals(that.first);
    }

    @Override
    public int hashCode() {
        int result = wrapper.hashCode();
        result = 31 * result + first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }
}
