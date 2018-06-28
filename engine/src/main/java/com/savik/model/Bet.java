package com.savik.model;

import com.savik.domain.BookmakerType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Bet {
    BookmakerType bookmakerType;

    BookmakerCoeff bookmakerCoeff;
}
