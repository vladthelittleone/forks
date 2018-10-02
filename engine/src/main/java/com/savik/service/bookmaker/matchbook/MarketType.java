package com.savik.service.bookmaker.matchbook;

import com.fasterxml.jackson.annotation.JsonProperty;

enum MarketType {
    @JsonProperty("Handicap")
    HANDICAP,
    
    @JsonProperty("Half Time/Full Time")
    HALF_FULL_TIME,
    
    @JsonProperty("Correct Score")
    CORRECT_SCORE,
    
    @JsonProperty("Total")
    TOTAL,
    
    @JsonProperty("Half Time Total")
    FIRST_HALF_TOTAL,
    
    @JsonProperty("Match Odds")
    ONE_X_TWO,
    
    @JsonProperty("Half Time Result")
    FIRST_HALF_ONE_X_TWO,
    
    @JsonProperty("Both Teams To Score")
    BOTH_TO_SCORE,

    @JsonProperty("Winner")
    OUTRIGHT,

    @JsonProperty("Match Odds and BTTS")
    OUTRIGHT_BTTS,
    
    @JsonProperty("money_line")
    MONEY_LINE,
    
    @JsonProperty("place")
    PLACE,
    
    @JsonProperty("Team to Score First")
    TEAM_SCORE_FIRST,
    
    @JsonProperty("First Goalscorer")
    FIRST_GOALSCORER,
    
    @JsonProperty("Group Winner")
    GROUP_WINNER,
    
    @JsonProperty("To Qualify")
    TO_QUALIFY,
    
    // the same as handicap 0
    @JsonProperty("Draw No Bet")
    DRAW_NO_BET,
}
