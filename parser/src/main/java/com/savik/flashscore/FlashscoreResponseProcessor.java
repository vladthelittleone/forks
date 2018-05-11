package com.savik.flashscore;

import com.savik.Match;
import com.savik.repository.FutureMatchRepository;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FlashscoreResponseProcessor {

    @Autowired
    FutureMatchRepository futureMatchRepository;

    @Autowired
    Downloader downloader;


    public void process(SportConfig sportConfig, List<Match> matches) {

        List<Match> dbMatches = futureMatchRepository.findAllById(matches.stream()
                .map(Match::getFlashscoreId).collect(Collectors.toList()));

        for (final Match match : matches) {
            log.debug("Start process future match - " + match.getFlashscoreId());
            match.setSportType(sportConfig.getSportType());

            Optional<Match> dbMatchOptional = dbMatches.stream()
                    .filter(dbM -> dbM.getFlashscoreId().equals(match.getFlashscoreId())).findFirst();

            if (dbMatchOptional.isPresent()) {
                Match dbMatch = dbMatchOptional.get();
                log.debug("Match was found in db - " + dbMatch);
                if (dbMatch.getMatchStatus() != match.getMatchStatus()) {
                    log.debug("Match status was changed: old: " + dbMatch.getMatchStatus() +
                            ", new: " + match.getMatchStatus());

                    dbMatch.setMatchStatus(match.getMatchStatus());
                    futureMatchRepository.save(dbMatch);
                }
            } else {
                log.debug("Match wasn't found in db");
                Match readyToSave = fillData(match);
                Match saved = futureMatchRepository.save(readyToSave);
                log.debug("Match was saved into db: " + saved);
            }

            log.debug("Finished process future match - " + match.getFlashscoreId());
        }
    }


    private Match fillData(Match match) {
        Document html = downloader.downloadMatchHtml(match.getFlashscoreId());
        String homeTeamId = FlashscoreUtils.getHomeTeamId(html);
        String guestTeamId = FlashscoreUtils.getGuestTeamId(html);
        match.getHomeTeam().setFlashscoreId(homeTeamId);
        match.getHomeTeam().setSportType(match.getSportType());
        match.getGuestTeam().setFlashscoreId(guestTeamId);
        match.getGuestTeam().setSportType(match.getSportType());
        return match;
    }

}
