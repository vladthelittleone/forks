package com.savik.service.bookmaker.matchbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchbookEventsResponse {
    Integer total;
    List<Event> events;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Event {
    Long id;

    String name;

    @JsonProperty("sport-id")
    Integer sportId;

    LocalDateTime start;

    @JsonProperty("in-running-flag")
    boolean isLive;

    @JsonProperty("meta-tags")
    List<MatchbookNavigationEntry> tags;

    String status;
    
    List<Market> markets;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Market {
    Long id;
    
    @JsonProperty("event-id")
    Long eventId;

    String name;

    @JsonProperty("sport-id")
    Integer sportId;

    LocalDateTime start;

    @JsonProperty("live")
    boolean isLive;

    String status;
    
    String type;
    
    @JsonProperty("market-type")
    String marketType;

    List<Runner> runners;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Runner {
    Long id;
    
    @JsonProperty("event-id")
    Long eventId;
    
    @JsonProperty("market-id")
    Long marketId;

    String name;
    
    @JsonProperty("live")
    boolean isLive;

    String status;

    List<Price> prices;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Price {
    
    String currency; // enum
    
    @JsonProperty("odds-type")
    String oddsType;
    
    @JsonProperty("decimal-odds")
    Double decimalOdds;
    
    String side; // enum
    
    @JsonProperty("available-amount")
    Double availableAmount;

}
