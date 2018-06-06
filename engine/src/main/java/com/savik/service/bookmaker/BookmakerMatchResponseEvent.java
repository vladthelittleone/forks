package com.savik.service.bookmaker;

import com.savik.domain.Match;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmakerMatchResponseEvent {

    BookmakerMatchResponse bookmakerMatchResponse;

    Match match;
}
