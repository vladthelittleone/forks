package com.savik.events;

import com.savik.service.bookmaker.BookmakerMatch;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmakerMatchNotFoundEvent {

    BookmakerMatch bookmakerMatch;
}
