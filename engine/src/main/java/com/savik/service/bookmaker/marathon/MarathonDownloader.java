package com.savik.service.bookmaker.marathon;

import com.savik.domain.SportType;
import com.savik.http.HttpClient;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
class MarathonDownloader {

    @Autowired
    HttpClient httpClient;

    @Autowired
    MarathonConfig marathonConfig;

    public MarathonResponse downloadMatch(String marathonMatchId) {
        final Document match = httpClient.post(marathonConfig.getMarketUrl(), Collections.singletonMap("treeId", marathonMatchId));
        return new MarathonResponse(match);
    }

    public MarathonResponse downloadLiveMatch(String marathonMatchId) {
        final Document match = httpClient.post(marathonConfig.getLiveMarketUrl(), Collections.singletonMap("treeId", marathonMatchId));
        return new MarathonResponse(match);
    }

    public MarathonResponse downloadPrematchMatchesBySport(SportType sportType) {
        String resultUrl = marathonConfig.getSportUrl(sportType);
        final Document antibot = httpClient.getAntibot(resultUrl);
        return new MarathonResponse(antibot);
    }

    public MarathonResponse downloadLiveMatchesBySport(SportType sportType) {
        String resultUrl = marathonConfig.getLiveSportUrl(sportType);
        final Document antibot = httpClient.getAntibot(resultUrl);
        return new MarathonResponse(antibot);
    }

}
