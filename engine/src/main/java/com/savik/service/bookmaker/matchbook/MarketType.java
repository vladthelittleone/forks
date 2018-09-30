package com.savik.service.bookmaker.matchbook;

import com.fasterxml.jackson.annotation.JsonProperty;

enum MarketType {
    @JsonProperty("handicap")
    HANDICAP,
    @JsonProperty("half_time_full_time")
    HALF_FULL_TIME,
    @JsonProperty("correct_score")
    CORRECT_SCORE,
    @JsonProperty("total")
    TOTAL,
    @JsonProperty("one_x_two")
    ONE_X_TWO,
    @JsonProperty("both_to_score")
    BOTH_TO_SCORE,
    @JsonProperty("outright")
    OUTRIGHT,
    @JsonProperty("money_line")
    MONEY_LINE,
    @JsonProperty("place")
    PLACE,
}
