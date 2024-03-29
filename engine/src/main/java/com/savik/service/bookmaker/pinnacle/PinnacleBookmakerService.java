package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.BookmakerType;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import com.savik.service.bookmaker.FastBookmaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@FastBookmaker
public class PinnacleBookmakerService extends BookmakerService {

    @Autowired
    PinnacleApi api;

    @Override
    protected BookmakerType getBookmakerType() {
        return BookmakerType.PINNACLE;
    }

    @Override
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatchWrapper match) {
        return api.parseMatch(match);
    }

    @Scheduled(fixedDelay = 1000 * 15, initialDelay = 60_000)
    public void refreshCacheOdds() {
        api.refreshCacheOdds();
    }
}
