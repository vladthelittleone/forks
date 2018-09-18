package com.savik.service.bookmaker.sbobet;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class SbobetBookmakerService extends BookmakerService {

    @Autowired
    SbobetParser sbobetParser;

    @Autowired
    SbobetDownloader downloader;

    @Autowired
    SbobetCache cache;

    @Override
    protected BookmakerType getBookmakerType() {
        return BookmakerType.SBOBET;
    }

    @Override
    public Optional<BookmakerMatchResponse> handle(BookmakerMatchWrapper bookmakerMatchWrapper) {
        Optional<BookmakerMatchResponse> bookmakerMatchResponse = findMatchInCache(bookmakerMatchWrapper);
        if (!bookmakerMatchResponse.isPresent() && !cache.dayWasParsed(bookmakerMatchWrapper.getDaysFromToday())) {
            bookmakerMatchResponse = tryToFindMatch(bookmakerMatchWrapper);
        }

        return bookmakerMatchResponse
                .flatMap(mR -> downloadAndParseSingleMatch(bookmakerMatchWrapper, mR))
                .flatMap(
                        mR -> {
                            cache.add(bookmakerMatchWrapper.getDaysFromToday(), mR);
                            return Optional.of(mR);
                        }
                );
    }

    private synchronized Optional<BookmakerMatchResponse> tryToFindMatch(BookmakerMatchWrapper bookmakerMatchWrapper) {
        Match match = bookmakerMatchWrapper.getMatch();
        log.debug("Start parsing sport day page: sport={}, days from today={}", match.getSportType(), bookmakerMatchWrapper.getDaysFromToday());
        List<BookmakerMatchResponse> matches = sbobetParser.getMatchesBySport(match.getSportType(), bookmakerMatchWrapper.getDaysFromToday());
        log.debug("Matches were parsed, amount: " + matches.size());
        cache.addAll(bookmakerMatchWrapper.getDaysFromToday(), matches);
        return findMatchInCache(bookmakerMatchWrapper);
    }

    private Optional<BookmakerMatchResponse> findMatchInCache(BookmakerMatchWrapper bookmakerMatchWrapper) {
        return cache.find(bookmakerMatchWrapper);
    }

    private Optional<BookmakerMatchResponse> downloadAndParseSingleMatch(BookmakerMatchWrapper bookmakerMatchWrapper, BookmakerMatchResponse bookmakerMatchResponse) {
        final Optional<BookmakerMatchResponse> response =
                sbobetParser.downloadAndParseSingleMatch(bookmakerMatchResponse.getBookmakerMatchId(), bookmakerMatchWrapper);
        log.debug("Match was parsed: " + bookmakerMatchWrapper.getDefaultLogString());
        log.trace("Match was parsed: " + bookmakerMatchResponse);
        return response;
    }
}
    






    

