package com.savik.service.bookmaker.matchbook;

import com.savik.domain.BookmakerType;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;

import java.util.Optional;

import static com.savik.domain.BookmakerType.MATCHBOOK;

//@Service
public class MatchbookBookmakerService extends BookmakerService {

    @Override
    protected BookmakerType getBookmakerType() {
        return MATCHBOOK;
    }

    @Override
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatchWrapper match) {
        return Optional.empty();
    }
}
