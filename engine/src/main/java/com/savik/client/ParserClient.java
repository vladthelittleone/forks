package com.savik.client;

import com.savik.domain.Match;
import com.savik.filter.MatchFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("parser")
@Component
public interface ParserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/matches")
    List<Match> getMatches(@RequestParam("matchFilter") MatchFilter matchFilter);
}
