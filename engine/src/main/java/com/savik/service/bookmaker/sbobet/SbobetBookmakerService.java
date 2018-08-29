package com.savik.service.bookmaker.sbobet;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
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
    public Optional<BookmakerMatchResponse> handle(BookmakerMatch bookmakerMatch) {
        Optional<BookmakerMatchResponse> bookmakerMatchResponse = findMatchInCache(bookmakerMatch);
        if (!bookmakerMatchResponse.isPresent() && !cache.dayWasParsed(bookmakerMatch.getDaysFromToday())) {
            bookmakerMatchResponse = tryToFindMatch(bookmakerMatch);
        }

        return bookmakerMatchResponse
                .flatMap(mR -> downloadAndParseSingleMatch(bookmakerMatch, mR))
                .flatMap(
                        mR -> {
                            cache.add(bookmakerMatch.getDaysFromToday(), mR);
                            return Optional.of(mR);
                        }
                );
    }

    private synchronized Optional<BookmakerMatchResponse> tryToFindMatch(BookmakerMatch bookmakerMatch) {
        Match match = bookmakerMatch.getMatch();
        log.debug("Start parsing sport day page: sport={}, days from today={}", match.getSportType(), bookmakerMatch.getDaysFromToday());
        List<BookmakerMatchResponse> matches = sbobetParser.getMatchesBySport(match.getSportType(), bookmakerMatch.getDaysFromToday());
        log.debug("Matches were parsed, amount: " + matches.size());
        cache.addAll(bookmakerMatch.getDaysFromToday(), matches);
        return findMatchInCache(bookmakerMatch);
    }

    private Optional<BookmakerMatchResponse> findMatchInCache(BookmakerMatch bookmakerMatch) {
        return cache.find(bookmakerMatch);
    }

    private Optional<BookmakerMatchResponse> downloadAndParseSingleMatch(BookmakerMatch bookmakerMatch, BookmakerMatchResponse bookmakerMatchResponse) {
        final Optional<BookmakerMatchResponse> response =
                sbobetParser.downloadAndParseSingleMatch(bookmakerMatchResponse.getBookmakerMatchId(), bookmakerMatch);
        log.debug("Match was parsed: " + bookmakerMatch.getDefaultLogString());
        log.trace("Match was parsed: " + bookmakerMatchResponse);
        return response;
    }
}
    






    

