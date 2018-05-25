package com.savik.domain;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class BookmakerLeague {

    @EmbeddedId
    BookmakerPK id;

    @NotNull
    String name;

    String bookmakerId;

}


