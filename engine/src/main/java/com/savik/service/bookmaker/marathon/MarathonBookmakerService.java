package com.savik.service.bookmaker.marathon;

import com.savik.domain.BookmakerType;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.savik.domain.BookmakerType.MARATHON;

@Component
public class MarathonBookmakerService extends BookmakerService {
    
    @Autowired
    MarathonDownloader marathonDownloader;
    
    @Override
    protected BookmakerType getBookmakerType() {
        return MARATHON;
    }

    @Override
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatch match) {
        Document download = marathonDownloader.download("6956051");
        return Optional.empty();
    }
}
