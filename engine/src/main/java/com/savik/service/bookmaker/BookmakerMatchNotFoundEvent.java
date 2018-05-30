package com.savik.service.bookmaker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmakerMatchNotFoundEvent {

    BookmakerMatch bookmakerMatch;
}
