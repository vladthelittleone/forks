package com.savik.domain;


import com.savik.domain.validation.FlashscoreId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    Team guestTeam;

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


}
