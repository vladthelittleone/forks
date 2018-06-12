package com.savik.events;

import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmakerMatchResponseEvent {

    BookmakerMatchResponse bookmakerMatchResponse;

    Match match;
}
