package com.savik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {

    @Id
    String flashscoreId;

    @NotNull
    String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    SportType sportType;

}
