package com.savik.service.bookmaker.matchbook;

import com.savik.domain.BookmakerType;
import com.savik.domain.SportType;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import com.savik.service.bookmaker.FastBookmaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.savik.domain.BookmakerType.MATCHBOOK;

@Service
@FastBookmaker
public class MatchbookBookmakerService extends BookmakerService {

    @Autowired
    MatchbookDownloader downloader;

    @Autowired
    MatchbookCache cache;

    @Autowired
    MatchbookParser parser;

    @Override
    protected BookmakerType getBookmakerType() {
        return MATCHBOOK;
    }

    @Override
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatchWrapper match) {
        downloadSportEvents(match.getMatch().getSportType(), false);
        return findMatchInCache(match);
    }

    private Optional<BookmakerMatchResponse> findMatchInCache(BookmakerMatchWrapper bookmakerMatchWrapper) {
        return cache.find(bookmakerMatchWrapper);
    }


    @Scheduled(fixedDelay = 1000 * 15, initialDelay = 5_000)
    public void refreshCacheOdds() {
        final List<SportType> sportTypes = Arrays.asList(SportType.values());
        for (SportType sportType : sportTypes) {
            downloadSportEvents(sportType, true);
        }
    }

    private synchronized void downloadSportEvents(SportType sportType, boolean updateAnyway) {
        if (updateAnyway || !cache.hasSportEvents(sportType)) {
            final MatchbookEventsResponse response = downloader.getEvents(sportType);
            final List<BookmakerMatchResponse> events = parser.parse(response.getEvents());
            cache.addAll(sportType, events);
        }
    }

}
