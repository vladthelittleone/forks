package com.savik;


import com.savik.validation.FlashscoreId;
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
