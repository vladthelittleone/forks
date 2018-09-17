package com.savik.domain;


import com.savik.domain.validation.FlashscoreId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    @Id
    @FlashscoreId
    String flashscoreId;

    @NotNull
    @FlashscoreId
    String flashscoreLeagueId;

    @NotNull
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Team homeTeam;

    @NotNull
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Team awayTeam;

    @NotNull
    LocalDateTime date;

    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    SportType sportType;

    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    MatchStatus matchStatus;

    public int getDaysFromToday() {
        LocalDateTime matchDate = getDate();
        int between = Period.between(LocalDate.now(), matchDate.toLocalDate()).getDays();
        return between;
    }

    public String getDefaultLogString() {
        return String.format("%s, %s - %s", getFlashscoreId(), homeTeam.getName(), awayTeam.getName());
    }

    @Override
    public String toString() {
        return "Match{" +
                "id='" + flashscoreId + '\'' +
                ", leaguedId='" + flashscoreLeagueId + '\'' +
                ", home=" + homeTeam +
                ", away=" + awayTeam +
                ", date=" + date +
                ", sport=" + sportType +
                ", status=" + matchStatus +
                '}';
    }
}
