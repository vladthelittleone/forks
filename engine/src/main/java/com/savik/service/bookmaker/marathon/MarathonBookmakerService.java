package com.savik.service.bookmaker.marathon;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.savik.domain.BookmakerType.MARATHON;

//@Component
@Log4j2
public class MarathonBookmakerService extends BookmakerService {

    @Autowired
    MarathonDownloader downloader;

    @Autowired
    MarathonParser parser;

    @Autowired
    MarathonCache cache;

    @Override
    protected BookmakerType getBookmakerType() {
        return MARATHON;
    }

    @Override
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatch bookmakerMatch) {
        Optional<BookmakerMatchResponse> bookmakerMatchResponse = findMatchInCache(bookmakerMatch);
        if (!bookmakerMatchResponse.isPresent()) {
            bookmakerMatchResponse = tryToFindMatch(bookmakerMatch);
        }

        return bookmakerMatchResponse
                .flatMap(mR -> downloadAndParseSingleMatch(bookmakerMatch, mR))
                .flatMap(
                        mR -> {
                            cache.add(mR);
                            return Optional.of(mR);
                        }
                );
        
        
    }

    private Optional<BookmakerMatchResponse> findMatchInCache(BookmakerMatch bookmakerMatch) {
        return cache.find(bookmakerMatch);
    }

    private Optional<BookmakerMatchResponse> tryToFindMatch(BookmakerMatch bookmakerMatch) {
        Match match = bookmakerMatch.getMatch();
        log.debug("Start parsing sport day page: sport={}, days from today={}", match.getSportType(), bookmakerMatch.getDaysFromToday());
        final List<BookmakerMatchResponse> matches = parser.getMatchesBySport(match.getSportType());
        log.debug("Matches were parsed, amount: " + matches.size());
        cache.addAll(matches);
        return findMatchInCache(bookmakerMatch);
    }

    private Optional<BookmakerMatchResponse> downloadAndParseSingleMatch(BookmakerMatch bookmakerMatch, BookmakerMatchResponse bookmakerMatchResponse) {
        final List<BookmakerCoeff> bookmakerCoeffs = parser.downloadAndParseMatch(bookmakerMatchResponse.getBookmakerMatchId(), bookmakerMatch);
        bookmakerMatchResponse.setBookmakerCoeffs(bookmakerCoeffs);
        log.debug("Match was parsed: " + bookmakerMatchResponse);
        return Optional.of(bookmakerMatchResponse);
    }

}
