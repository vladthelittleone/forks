package com.savik.service.bookmaker.pinnacle;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FixtureResponse {
    Integer sportId;
    Integer last;
    List<FixtureLeague> league;
}

@Data
class FixtureLeague {
    Integer id;
    String name;
    List<FixtureEvent> events;
}

@Data
class FixtureEvent {
    Integer id;
    Integer parentId;
    String home;
    String away;
    Integer liveStatus;
    String status;
    LocalDateTime starts;
}
