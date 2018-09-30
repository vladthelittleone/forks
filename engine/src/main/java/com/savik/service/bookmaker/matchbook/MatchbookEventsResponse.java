package com.savik.service.bookmaker.matchbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    
    Long bookmakerLeagueId;

    String name;
    
    String bookmakerHomeTeamName;
    
    String bookmakerAwayTeamName;

    @JsonProperty("sport-id")
    Integer sportId;

    LocalDateTime start;

    @JsonProperty("in-running-flag")
    boolean isLive;

    @JsonProperty("meta-tags")
    List<MatchbookNavigationEntry> tags;

    Status status;
    
    List<Market> markets;

    public void setTags(List<MatchbookNavigationEntry> tags) {
        this.tags = tags;
        Optional<MatchbookNavigationEntry> entry = tags.stream().filter(t -> t.getType() == TagType.COMPETITION)
                .findFirst();
        this.bookmakerLeagueId = entry.map(MatchbookNavigationEntry::getId).orElse(null);
    }

    public void setName(String name) {
        this.name = name;
        final String[] names = name.split("vs");
        if(names.length == 2) {
            this.bookmakerHomeTeamName = names[0].trim();
            this.bookmakerAwayTeamName = names[1].trim();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
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

    LocalDateTime start;

    @JsonProperty("live")
    boolean isLive;

    Status status;

    Type type;
    
    @JsonProperty("market-type")
    MarketType marketType;

    List<Runner> runners;

    Double handicap;

    @JsonProperty("asian-handicap")
    String asianHandicap;
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

    Double handicap;

    @JsonProperty("asian-handicap")
    String asianHandicap;
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

    Side side;
    
    @JsonProperty("available-amount")
    Double availableAmount;

}
