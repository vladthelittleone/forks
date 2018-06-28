package com.savik.events;

import com.savik.domain.Match;
import com.savik.model.Bet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ForkFoundEvent {

    Match match;

    Bet first;

    Bet second;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForkFoundEvent that = (ForkFoundEvent) o;

        if (!match.equals(that.match)) return false;
        if (!first.equals(that.first) && !first.equals(that.second)) return false;
        return second.equals(that.second) || second.equals(that.first);
    }

    @Override
    public int hashCode() {
        int result = match.hashCode();
        result = 31 * result + first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }
}
