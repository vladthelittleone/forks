package com.savik;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FutureMatch {

    @Id
    String flashscoreId;

    @NotNull
    String flashscoreLeagueId;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    Team homeTeam;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    Team guestTeam;

    @NotNull
    LocalDateTime date;

    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    SportType sportType;

    @NotNull
    @Enumerated(EnumType.STRING)
    MatchStatus matchStatus;



}
