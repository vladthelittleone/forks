package com.savik.service;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.service.bookmaker.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatchResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ForksService {

    Map<String, Map<BookmakerType, List<BookmakerCoeff>>> forks = new HashMap<>();

    public void handle(BookmakerMatchResponse bookmakerMatchResponse, Match match) {
        forks.putIfAbsent(match.getFlashscoreId(), new HashMap<>());
        Map<BookmakerType, List<BookmakerCoeff>> matchMap = forks.get(match.getFlashscoreId());

        matchMap.put(bookmakerMatchResponse.getBookmakerType(), bookmakerMatchResponse.getBookmakerCoeffs());

        List<Entry<BookmakerType, List<BookmakerCoeff>>> otherBookmakers = matchMap.entrySet().stream()
                .filter(eS -> eS.getKey() != bookmakerMatchResponse.getBookmakerType()).collect(Collectors.toList());

        for (Entry<BookmakerType, List<BookmakerCoeff>> otherBookmaker : otherBookmakers) {
            List<BookmakerCoeff> otherBookCoeffs = otherBookmaker.getValue();

            for (BookmakerCoeff otherBookCoeff : otherBookCoeffs) {

            }




        }

    }
}
