package com.savik.service.bookmaker.pinnacle;

import lombok.Data;

import java.util.List;

@Data
class FixtureResponse {
    Integer sportId;
    Integer last;
    List<FixtureLeague> league;
}
