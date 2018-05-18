package com.savik.service.bookmaker;

import com.savik.domain.BookmakerType;
import org.springframework.stereotype.Component;

@Component
public abstract class BookmakerService {

    abstract BookmakerType getBookmakerType();
}
