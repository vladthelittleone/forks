package com.savik.domain;

import com.savik.domain.validation.FlashscoreId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class BookmakerTeamPK implements Serializable {

    @FlashscoreId
    String teamFlashscoreId;

    @Enumerated(EnumType.STRING)
    BookmakerType bookmakerType;
}
