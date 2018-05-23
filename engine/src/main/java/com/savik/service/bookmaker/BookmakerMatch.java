package com.savik.service.bookmaker;

import com.savik.domain.BookmakerTeam;
import com.savik.domain.Match;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmakerMatch {

    Match match;

    BookmakerTeam homeTeam;

    BookmakerTeam guestTeam;
}
