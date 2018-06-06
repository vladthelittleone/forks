package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
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
        log.debug("BookmakerMatchResponseEvent received: " + event);
        BookmakerMatchResponse bookmakerMatchResponse = event.getBookmakerMatchResponse();
        Match match = event.getMatch();
        matches.putIfAbsent(match.getFlashscoreId(), new HashMap<>());
        Map<BookmakerType, List<BookmakerCoeff>> matchBookmakersCoeffs = matches.get(match.getFlashscoreId());
        List<BookmakerCoeff> bookmakerCoeffs = bookmakerMatchResponse.getBookmakerCoeffs();
        matchBookmakersCoeffs.put(bookmakerMatchResponse.getBookmakerType(), bookmakerCoeffs);
        List<Entry<BookmakerType, List<BookmakerCoeff>>> otherBookmakers = matchBookmakersCoeffs.entrySet().stream()
                .filter(eS -> eS.getKey() != bookmakerMatchResponse.getBookmakerType()).collect(Collectors.toList());

        List<ForkFoundEvent> events = new ArrayList<>();
        for (Entry<BookmakerType, List<BookmakerCoeff>> otherBookmaker : otherBookmakers) {
            List<BookmakerCoeff> otherBookCoeffs = otherBookmaker.getValue();

            for (BookmakerCoeff newBookmakerCoeff : bookmakerCoeffs) {
                for (BookmakerCoeff otherBookCoeff : otherBookCoeffs) {
                    if (otherBookCoeff.isFork(newBookmakerCoeff)) {
                        events.add(new ForkFoundEvent());
                    } else {
                    }
                }
            }
        }
        return events;
    }
}
