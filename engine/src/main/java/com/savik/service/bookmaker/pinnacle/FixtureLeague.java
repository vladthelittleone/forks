package com.savik.service.bookmaker.pinnacle;

import lombok.Data;

import java.util.List;

@Data
class FixtureLeague {
    Integer id;
    String name;
    List<FixtureEvent> events;
}
