package com.savik;

import com.savik.validation.FlashscoreId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {

    @Id
    @FlashscoreId
    String flashscoreId;

    @NotNull
    String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    SportType sportType;

}
