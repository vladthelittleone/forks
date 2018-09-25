package com.savik.service.bookmaker.marathon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jsoup.nodes.Document;

@AllArgsConstructor
@Getter
class MarathonResponse {
    final Document download;
}
