package com.savik;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode
public class FutureMatch {

    @Id
    String flashscoreId;

    @NotNull
    @OneToOne
    Team homeTeam;

    @NotNull
    @OneToOne
    Team guestTeam;

    @NotNull
    LocalDateTime date;

    @NotNull
    @Enumerated(EnumType.STRING)
    SportType sportType;

    @NotNull
    @Enumerated(EnumType.STRING)
    MatchStatus matchStatus;



}
