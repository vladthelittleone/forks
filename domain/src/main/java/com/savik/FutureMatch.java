package com.savik;


import com.savik.domain.Identifiable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode
public class FutureMatch extends Identifiable {

    @NotNull
    @OneToOne
    Team homeTeam;

    @NotNull
    @OneToOne
    Team guestTeam;

    @NotNull
    String flashscoreId;

    @NotNull
    LocalDateTime date;

    @NotNull
    @Enumerated(EnumType.STRING)
    SportType sportType;



}
