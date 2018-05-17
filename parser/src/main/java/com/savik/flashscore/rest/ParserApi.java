package com.savik.flashscore.rest;

import com.savik.domain.Match;
import com.savik.filter.MatchFilter;
import com.savik.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ParserApi {

    @Autowired
    MatchRepository matchRepository;

    @GetMapping("/matches")
    public List<Match> greeting(MatchFilter filter) {
        return matchRepository.findAll(filter);
    }
}
