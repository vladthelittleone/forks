package com.savik.service.bookmaker.marathon;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.service.bookmaker.BookmakerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.savik.domain.BookmakerType.MARATHON;

@Component
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
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatchWrapper bookmakerMatchWrapper) {
        Optional<BookmakerMatchResponse> bookmakerMatchResponse = findMatchInCache(bookmakerMatchWrapper);
        if (!bookmakerMatchResponse.isPresent() && cache.isEmpty()) {
            bookmakerMatchResponse = tryToFindMatch(bookmakerMatchWrapper);
        }
        return bookmakerMatchResponse
                .flatMap(mR -> downloadAndParseSingleMatch(bookmakerMatchWrapper, mR))
                .flatMap(
                        mR -> {
                            cache.add(mR);
                            return Optional.of(mR);
                        }
                );
    }

    private Optional<BookmakerMatchResponse> findMatchInCache(BookmakerMatchWrapper bookmakerMatchWrapper) {
        return cache.find(bookmakerMatchWrapper);
    }

    private synchronized Optional<BookmakerMatchResponse> tryToFindMatch(BookmakerMatchWrapper bookmakerMatchWrapper) {
        Match match = bookmakerMatchWrapper.getMatch();
        log.debug("Start parsing sport day page: sport={}, days from today={}", match.getSportType(), bookmakerMatchWrapper.getDaysFromToday());
        MarathonResponse marathonResponse;
        if (match.getMatchStatus() == MatchStatus.PREMATCH) {
            marathonResponse = downloader.downloadPrematchMatchesBySport(match.getSportType());
        } else if (match.getMatchStatus() == MatchStatus.LIVE) {
            marathonResponse = downloader.downloadLiveMatchesBySport(match.getSportType());
        } else {
            throw new RuntimeException("Match status is incorrect: " + match);
        }
        final List<BookmakerMatchResponse> matches = parser.parseMatches(marathonResponse);
        log.debug("Matches were parsed, amount: " + matches.size());
        cache.addAll(matches);
        return findMatchInCache(bookmakerMatchWrapper);
    }

    private Optional<BookmakerMatchResponse> downloadAndParseSingleMatch(BookmakerMatchWrapper bookmakerMatchWrapper, BookmakerMatchResponse bookmakerMatchResponse) {
        Match match = bookmakerMatchWrapper.getMatch();
        MarathonResponse marathonResponse;
        if (match.getMatchStatus() == MatchStatus.PREMATCH) {
            marathonResponse = downloader.downloadMatch(bookmakerMatchResponse.getBookmakerMatchId());
        } else if (match.getMatchStatus() == MatchStatus.LIVE) {
            marathonResponse = downloader.downloadLiveMatch(bookmakerMatchResponse.getBookmakerMatchId());
        } else {
            throw new RuntimeException("Match status is incorrect: " + match);
        }
        final List<BookmakerCoeff> bookmakerCoeffs = parser.downloadAndParseMatch(marathonResponse, bookmakerMatchWrapper);
        bookmakerMatchResponse.setBookmakerCoeffs(bookmakerCoeffs);
        log.debug("Match was parsed: " + bookmakerMatchWrapper.getDefaultLogString());
        log.trace("Match was parsed: " + bookmakerMatchResponse);
        return Optional.of(bookmakerMatchResponse);
    }

}
