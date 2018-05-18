package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import org.springframework.stereotype.Service;

@Service
public class SbobetBookmakerService extends BookmakerService {
    @Override
    BookmakerType getBookmakerType() {
        return BookmakerType.SBOBET;
    }
}
