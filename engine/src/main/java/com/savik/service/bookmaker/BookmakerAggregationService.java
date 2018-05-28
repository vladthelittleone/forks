package com.savik.service.bookmaker;

import com.savik.domain.Match;
import com.savik.domain.SportType;
import com.savik.service.bookmaker.sbobet.SbobetBookmakerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Log4j2
public class BookmakerAggregationService {

    List<BookmakerService> bookmakerServices;

    public BookmakerAggregationService(List<BookmakerService> bookmakerServices) {
        if (bookmakerServices.isEmpty()) {
            log.info("bookmaker services should not be empty");
            throw new IllegalArgumentException();
        }
        log.debug("Aggregation bookmaker service was initiate by bookmaker services:"
                + bookmakerServices.stream().map(BookmakerService::getBookmakerType).collect(toList()));
        this.bookmakerServices = bookmakerServices;
    }


    @Autowired
    SbobetBookmakerService sbobetBookmakerService;

    @Async
    public void handle(Match match) {
        sbobetBookmakerService.handle(
                BookmakerMatch.builder()
                        .match(Match.builder().sportType(SportType.FOOTBALL).date(LocalDateTime.now()).build())
                        .build()
        );


    }
}
