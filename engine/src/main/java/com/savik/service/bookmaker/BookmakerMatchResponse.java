package com.savik.service.bookmaker;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookmakerMatchResponse {

    String bookmakerMatchId;

    String bookmakerLeagueId;

    String bookmakerHomeTeamName;

    String bookmakerGuestTeamName;

    List<BookmakerCoeff> bookmakerCoeffs;
}
