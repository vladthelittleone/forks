package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.BookmakerType;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PinnacleBookmakerService extends BookmakerService {

    @Autowired
    PinnacleApi api;

    @Override
    protected BookmakerType getBookmakerType() {
        return BookmakerType.PINNACLE;
    }

    @Override
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatch match) {
        return Optional.empty();
    }
}
