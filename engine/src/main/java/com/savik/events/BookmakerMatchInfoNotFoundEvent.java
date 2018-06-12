package com.savik.events;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmakerMatchInfoNotFoundEvent {

    Match match;

    BookmakerType bookmakerType;
}
