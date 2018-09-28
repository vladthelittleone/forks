package com.savik.service.bookmaker.matchbook;

import com.savik.domain.BookmakerType;
import com.savik.domain.SportType;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.savik.domain.BookmakerType.MATCHBOOK;

@Service
public class MatchbookBookmakerService extends BookmakerService {
    
    @Autowired
    MatchbookDownloader downloader;

    @Override
    protected BookmakerType getBookmakerType() {
        return MATCHBOOK;
    }

    @Override
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatchWrapper match) {
        return Optional.empty();
    }


    @Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 5_000)
    public void refreshCacheOdds() {
        final List<SportType> sportTypes = Arrays.asList(SportType.values());
        for (SportType sportType : sportTypes) {
            final MatchbookEventsResponse events = downloader.getEvents(sportType);
        }
    }
    
}
