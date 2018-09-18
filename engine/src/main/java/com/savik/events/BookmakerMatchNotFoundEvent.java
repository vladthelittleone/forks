package com.savik.events;

import com.savik.service.bookmaker.BookmakerMatchWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmakerMatchNotFoundEvent {

    BookmakerMatchWrapper bookmakerMatchWrapper;
}
