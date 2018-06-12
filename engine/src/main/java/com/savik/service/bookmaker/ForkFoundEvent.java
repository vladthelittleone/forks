package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ForkFoundEvent {

    Match match;

    BookmakerType newBookmakerType;

    BookmakerCoeff newBookmakerCoeff;

    BookmakerType oldBookmakerType;

    BookmakerCoeff oldBookmakerCoeff;

}
