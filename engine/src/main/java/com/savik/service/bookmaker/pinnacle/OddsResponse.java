package com.savik.service.bookmaker.pinnacle;

import lombok.Data;

import java.util.List;

@Data
public class OddsResponse {
    Integer sportId;
    Long last;
    List<OddsLeague> leagues;

    public OddsEvent findEvent(FixtureEvent fixtureEvent) {
        for (OddsLeague fixtureLeague : leagues) {
            List<OddsEvent> events = fixtureLeague.getEvents();
            for (OddsEvent event : events) {
                if (event.getId().equals(fixtureEvent.getId())) {
                    return event;
                }
            }
        }
        throw new PinnacleException("Odd event not found, fixture id: " + fixtureEvent.getId());
    }
}

@Data
class OddsLeague {
    Integer id;
    List<OddsEvent> events;
}

@Data
class OddsEvent {
    Integer id;
    List<OddsPeriod> periods;
}

@Data
class OddsPeriod {
    Integer lineId;
    Integer number;
    Integer status;
    List<OddsSpread> spreads;
    OddsMoneyline moneyline;
    List<OddsTotal> totals;
    OddsTeamTotalBlock teamTotal;
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
