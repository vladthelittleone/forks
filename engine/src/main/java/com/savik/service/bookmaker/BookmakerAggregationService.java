package com.savik.service.bookmaker;

import com.savik.domain.Match;
import com.savik.service.bookmaker.sbobet.SbobetBookmakerService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

    @Async
    public void handle(Match match) {
        ThreadContext.put("match.id", match.getFlashscoreId());
        for (BookmakerService bookmakerService : bookmakerServices) {
            log.info(String.format("Start handling match. matchId: %s, bookmaker service: %s, ", match.getFlashscoreId(), bookmakerService.getBookmakerType()));
            bookmakerService.handle(match);
            log.info(String.format("Match was handled. matchId: %s, bookmaker service: %s", match.getFlashscoreId(), bookmakerService.getBookmakerType()));
        }
        ThreadContext.clearAll();
    }
}
