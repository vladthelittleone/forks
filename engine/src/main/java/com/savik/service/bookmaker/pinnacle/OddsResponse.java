package com.savik.service.bookmaker.pinnacle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OddsResponse {
    Integer sportId;
    Long last;
    List<OddsLeague> leagues;

    public OddsEvent findEvent(FixtureEvent fixtureEvent) {
        for (OddsLeague fixtureLeague : leagues) {
            final Optional<OddsEvent> optionalEvent = fixtureLeague.findEvent(fixtureEvent.getId());
            if (optionalEvent.isPresent()) {
                return optionalEvent.get();
            }
        }
        throw new PinnacleException("Odd event not found, fixture id: " + fixtureEvent.getId());
    }

    public void addLeague(OddsLeague newLeague) {
        if (leagues.stream().anyMatch(l -> l.getId().equals(newLeague.getId()))) {
            throw new IllegalArgumentException("League already exists: " + newLeague);
        }
        leagues = new ArrayList<>(leagues);
        leagues.add(newLeague);
    }

    public Optional<OddsLeague> findLeague(Integer id) {
        return leagues.stream().filter(l -> l.getId().equals(id)).findFirst();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OddsLeague {
    Integer id;
    List<OddsEvent> events;

    public Optional<OddsEvent> findEvent(Integer id) {
        return events.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public void addEvent(OddsEvent newEvent) {
        if (events.stream().anyMatch(l -> l.getId().equals(newEvent.getId()))) {
            throw new IllegalArgumentException("Event already exists: " + newEvent);
        }
        events = new ArrayList<>(events);
        events.add(newEvent);
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OddsEvent {
    Integer id;
    List<OddsPeriod> periods;

    public Optional<OddsPeriod> findPeriod(Integer number) {
        return periods.stream().filter(e -> e.getNumber().equals(number)).findFirst();
    }

    public void replacePeriod(OddsPeriod newPeriod) {
        final int index = periods.indexOf(newPeriod);
        if (index != -1) {
            periods.set(index, newPeriod);
        } else {
            periods = new ArrayList<>(periods);
            periods.add(newPeriod);
        }
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OddsPeriod {
    Integer lineId;
    Integer number;
    Integer status;
    List<OddsSpread> spreads;
    OddsMoneyline moneyline;
    List<OddsTotal> totals;
    OddsTeamTotalBlock teamTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OddsPeriod that = (OddsPeriod) o;

        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
}

@Data
class OddsSpread {
    Double hdp;
    Double home;
    Double away;
}

@Data
class OddsMoneyline {
    Double home;
    Double away;
    Double draw;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OddsTotal {
    Double points;
    Double over;
    Double under;
}

@Data
class OddsTeamTotalBlock {
    OddsTeamTotal home;
    OddsTeamTotal away;
}

@Data
class OddsTeamTotal {
    Double points;
    Double over;
    Double under;
}
