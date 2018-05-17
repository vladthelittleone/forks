package com.savik.flashscore;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savik.domain.Match;
import com.savik.filter.MatchFilter;
import com.savik.flashscore.rest.ParserApi;
import com.savik.repository.MatchRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.savik.flashscore.TestUtils.createMatch;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ParserApi.class)
public class MvcApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MatchRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testMatchJson() throws Exception {
        Match match = createMatch();

        List<Match> matches = Arrays.asList(match);

        given(repository.findAll(any(MatchFilter.class))).willReturn(matches);

        String jsonResponseContent = mvc.perform(get("/matches")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        List<Match> parsedMatches = objectMapper.readValue(jsonResponseContent, new TypeReference<List<Match>>() {
        });
        Match receivedMatch = parsedMatches.get(0);

        Assert.assertEquals(1, parsedMatches.size());
        Assert.assertEquals(match.getMatchStatus(), receivedMatch.getMatchStatus());
        Assert.assertEquals(match.getFlashscoreId(), receivedMatch.getFlashscoreId());
        Assert.assertEquals(match.getFlashscoreLeagueId(), receivedMatch.getFlashscoreLeagueId());
        Assert.assertEquals(match.getSportType(), receivedMatch.getSportType());
        Assert.assertEquals(match.getDate(), receivedMatch.getDate());
        Assert.assertEquals(match.getHomeTeam(), receivedMatch.getHomeTeam());
        Assert.assertEquals(match.getGuestTeam(), receivedMatch.getGuestTeam());


    }
}
