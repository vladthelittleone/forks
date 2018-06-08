package com.savik.flashscore;

import com.savik.domain.Match;
import com.savik.repository.MatchRepository;
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
    MatchRepository matchRepository;

    @Autowired
    ParserDownloader downloader;


    public void process(SportConfig sportConfig, List<Match> matches) {

        List<Match> dbMatches = matchRepository.findAllById(matches.stream()
                .map(Match::getFlashscoreId).collect(Collectors.toList()));

        for (final Match match : matches) {
            log.debug("Start process future match - " + match.getFlashscoreId());
            match.setSportType(sportConfig.getSportType());

            Optional<Match> dbMatchOptional = dbMatches.stream()
                    .filter(dbM -> dbM.getFlashscoreId().equals(match.getFlashscoreId())).findFirst();

            if (dbMatchOptional.isPresent()) {
                processExistentMatch(match, dbMatchOptional.get());
            } else {
                processNonexistentMatch(sportConfig, match);
            }
            log.debug("Finished process future match - " + match.getFlashscoreId());
        }
    }

    private void processExistentMatch(Match flashscoreMatch, Match dbMatch) {
        log.debug("Match was found in db - " + dbMatch);
        if (dbMatch.getMatchStatus() != flashscoreMatch.getMatchStatus()) {
            log.debug("Match status was changed: old: " + dbMatch.getMatchStatus() +
                    ", new: " + flashscoreMatch.getMatchStatus());

            dbMatch.setMatchStatus(flashscoreMatch.getMatchStatus());
            matchRepository.save(dbMatch);
        }
    }

    private void processNonexistentMatch(SportConfig sportConfig, Match match) {
        log.debug("Match wasn't found in db");
        Match readyToSave = fillData(sportConfig, match);
        Match saved = matchRepository.save(readyToSave);
        log.debug("Match was saved into db: " + saved);
    }


    private Match fillData(SportConfig sportConfig, Match match) {
        fillMatch(sportConfig, match);
        fillTeams(match);
        return match;
    }


    private void fillMatch(SportConfig sportConfig, Match match) {
        match.setSportType(sportConfig.getSportType());
    }

    private void fillTeams(Match match) {
        Document html = downloader.downloadMatchHtml(match.getFlashscoreId());
        String homeTeamId = FlashscoreUtils.getHomeTeamId(html);
        String guestTeamId = FlashscoreUtils.getGuestTeamId(html);
        match.getHomeTeam().setFlashscoreId(homeTeamId);
        match.getHomeTeam().setSportType(match.getSportType());
        match.getAwayTeam().setFlashscoreId(guestTeamId);
        match.getAwayTeam().setSportType(match.getSportType());
    }

}
