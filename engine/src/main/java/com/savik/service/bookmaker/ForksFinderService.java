package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.events.BookmakerMatchResponseEvent;
import com.savik.events.ForkFoundEvent;
import com.savik.model.Bet;
import com.savik.model.BookmakerCoeff;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ForksFinderService {

    Map<String, Map<BookmakerType, List<BookmakerCoeff>>> matches = new HashMap<>();

    @EventListener
    public List<ForkFoundEvent> handleBookmakerResponse(final BookmakerMatchResponseEvent event) {
        log.debug("Start handling new book match response: " + event);

        BookmakerMatchResponse bookmakerMatchResponse = event.getBookmakerMatchResponse();
        Match match = event.getMatch();
        Map<BookmakerType, List<BookmakerCoeff>> matchBookmakersCoeffs = getBookmakersCoeffs(match);
        List<BookmakerCoeff> eventBookmakerCoeffs = saveNewCoeffsAndGet(bookmakerMatchResponse, matchBookmakersCoeffs);
        log.debug("New received coeffs: " + eventBookmakerCoeffs);

        List<Entry<BookmakerType, List<BookmakerCoeff>>> otherBookmakers = matchBookmakersCoeffs.entrySet().stream()
                .filter(eS -> eS.getKey() != bookmakerMatchResponse.getBookmakerType()).collect(Collectors.toList());
        List<ForkFoundEvent> events = new ArrayList<>();
        for (Entry<BookmakerType, List<BookmakerCoeff>> otherBookmaker : otherBookmakers) {
            log.debug("Start comparing with bookmaker: " + otherBookmaker.getKey());
            List<BookmakerCoeff> otherBookCoeffs = otherBookmaker.getValue();
            for (BookmakerCoeff newBookmakerCoeff : eventBookmakerCoeffs) {
                for (BookmakerCoeff otherBookCoeff : otherBookCoeffs) {
                    if (!otherBookCoeff.isBetCompatibleByMeaning(newBookmakerCoeff)) {
                        continue;
                    }
                    if (otherBookCoeff.isBetCompatibleByValue(newBookmakerCoeff)) {
                        //log.debug(String.format("It's a fork compatible types: new=%s, old=%s", newBookmakerCoeff, otherBookCoeff));
                    } else {
                        //log.debug(String.format("It's not a fork by type: new=%s, old=%s", newBookmakerCoeff, otherBookCoeff));
                        continue;
                    }
                    if (otherBookCoeff.isFork(newBookmakerCoeff)) {
                        //log.debug(String.format("Fork is found: new=%s, old=%s: ", newBookmakerCoeff, otherBookCoeff));
                        events.add(
                                new ForkFoundEvent(
                                        match,
                                        new Bet(bookmakerMatchResponse.getBookmakerType(), newBookmakerCoeff),
                                        new Bet(otherBookmaker.getKey(), otherBookCoeff)
                                )
                        );
                    } else {
                        /*log.debug(String.format("It's not a fork by coeff: percentage=%s, new=%s, old=%s",
                                BookmakerCoeff.getForkPercentage(newBookmakerCoeff, otherBookCoeff), newBookmakerCoeff, otherBookCoeff));*/
                    }
                }
            }
        }
        return events;
    }

    private List<BookmakerCoeff> saveNewCoeffsAndGet(BookmakerMatchResponse bookmakerMatchResponse, Map<BookmakerType, List<BookmakerCoeff>> matchBookmakersCoeffs) {
        List<BookmakerCoeff> eventBookmakerCoeffs = bookmakerMatchResponse.getBookmakerCoeffs();
        matchBookmakersCoeffs.put(bookmakerMatchResponse.getBookmakerType(), eventBookmakerCoeffs);
        return eventBookmakerCoeffs;
    }

    private Map<BookmakerType, List<BookmakerCoeff>> getBookmakersCoeffs(Match match) {
        matches.putIfAbsent(match.getFlashscoreId(), new HashMap<>());
        return matches.get(match.getFlashscoreId());
    }
}
