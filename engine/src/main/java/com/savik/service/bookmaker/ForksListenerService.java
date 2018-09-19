package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.events.BookmakerMatchResponseEvent;
import com.savik.events.ForkFoundEvent;
import com.savik.model.Bet;
import com.savik.model.BookmakerCoeff;
import com.savik.model.BookmakerMatchWrapper;
import com.savik.utils.BookmakerUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
@Log4j2
public class ForksListenerService {


    @Autowired
    private ForksService forksService;

    private Map<String, Map<BookmakerType, BookMatchFullWrapper>> matches = new ConcurrentHashMap<>();

    //@Async
    @EventListener
    public void handle(final ForkFoundEvent event) {
        final Match match = event.getMatch();
        final Bet first = event.getFirst();
        final Bet second = event.getSecond();
        StringBuilder builder = new StringBuilder("\n\nFork was found: ");
        builder.append("percentage=").append(BookmakerUtils.formatFork(first.getBookmakerCoeff(), second.getBookmakerCoeff()))
                .append(" id = ").append(match.getFlashscoreId())
                .append(" hT = ").append(match.getHomeTeam().getName()).append(" aT = ").append(match.getAwayTeam().getName())
                .append("\n b1=").append(first).append("\n b2=").append(second).append("\n");
        log.info(builder.toString());
    }

    @EventListener
    public void handleBookmakerResponse(final BookmakerMatchResponseEvent event) {
        log.trace("Start handling new book match response: " + event);

        final BookmakerMatchResponse bookmakerMatchResponse = event.getBookmakerMatchResponse();
        final BookmakerMatchWrapper wrapper = event.getWrapper();
        final Match match = wrapper.getMatch();
        
        Map<BookmakerType, BookMatchFullWrapper> matchBookmakersCoeffs = getBookmakersCoeffs(match.getFlashscoreId());
        List<BookmakerCoeff> eventBookmakerCoeffs = saveNewCoeffsAndGet(bookmakerMatchResponse, wrapper, matchBookmakersCoeffs);
        log.trace("New received coeffs: " + eventBookmakerCoeffs);

        List<Map.Entry<BookmakerType, BookMatchFullWrapper>> otherBookmakers = matchBookmakersCoeffs.entrySet().stream()
                .filter(eS -> eS.getKey() != bookmakerMatchResponse.getBookmakerType()).collect(Collectors.toList());
        
        List<ForkFoundEvent> events = new ArrayList<>();
        for (Map.Entry<BookmakerType, BookMatchFullWrapper> otherBookmaker : otherBookmakers) {
            log.trace("Start comparing with bookmaker: " + otherBookmaker.getKey());
            List<BookmakerCoeff> otherBookCoeffs = otherBookmaker.getValue().getCoeffs();
            for (BookmakerCoeff newBookmakerCoeff : eventBookmakerCoeffs) {
                for (BookmakerCoeff otherBookCoeff : otherBookCoeffs) {
                    if (!otherBookCoeff.isBetCompatibleByMeaning(newBookmakerCoeff)) {
                        continue;
                    }
                    if (otherBookCoeff.isBetCompatibleByValue(newBookmakerCoeff)) {
                        log.trace(String.format("It's a fork compatible types: new=%s, old=%s", newBookmakerCoeff, otherBookCoeff));
                    } else {
                        log.trace(String.format("It's not a fork by type: new=%s, old=%s", newBookmakerCoeff, otherBookCoeff));
                        continue;
                    }
                    if (otherBookCoeff.isFork(newBookmakerCoeff)) {
                        log.trace(String.format("Fork is found: new=%s, old=%s: ", newBookmakerCoeff, otherBookCoeff));
                        events.add(
                                new ForkFoundEvent(
                                        match,
                                        new Bet(bookmakerMatchResponse.getBookmakerType(), newBookmakerCoeff, wrapper),
                                        new Bet(otherBookmaker.getKey(), otherBookCoeff, otherBookmaker.getValue().getMatchWrapper())
                                )
                        );
                    } else {
                        log.trace(String.format("It's not a fork by coeff: percentage=%s, new=%s, old=%s",
                                BookmakerUtils.getForkPercentage(newBookmakerCoeff.getCoeffValue(), otherBookCoeff.getCoeffValue()), newBookmakerCoeff, otherBookCoeff));
                    }
                }
            }
        }
        if (!events.isEmpty()) {
            forksService.verifyExistence(match, events);
        }
    }


    private List<BookmakerCoeff> saveNewCoeffsAndGet(
            BookmakerMatchResponse bookmakerMatchResponse,
            BookmakerMatchWrapper matchWrapper,
            Map<BookmakerType, BookMatchFullWrapper> matchBookmakersCoeffs) {
        List<BookmakerCoeff> eventBookmakerCoeffs = bookmakerMatchResponse.getBookmakerCoeffs();
        matchBookmakersCoeffs.put(
                bookmakerMatchResponse.getBookmakerType(),
                new BookMatchFullWrapper(eventBookmakerCoeffs, matchWrapper)
        );
        return eventBookmakerCoeffs;
    }

    private Map<BookmakerType, BookMatchFullWrapper> getBookmakersCoeffs(String flashscoreId) {
        matches.putIfAbsent(flashscoreId, new HashMap<>());
        return matches.get(flashscoreId);
    }
}

@AllArgsConstructor
@Getter
class BookMatchFullWrapper {
    List<BookmakerCoeff> coeffs;
    BookmakerMatchWrapper matchWrapper;
}
