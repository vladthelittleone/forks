package com.savik.flashscore;

import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import com.savik.domain.Team;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlashscoreResponseParser {

    private static final String JS_ROW_END = "~";

    private static final String JS_CELL_END = "¬";

    private static final String JS_INDEX = "÷";

    private static final String LEAGUE_INDEX = "ZA";

    private static final String EVENT_INDEX = "AA";

    private static final String HOME_INDEX = "CX";

    private static final String GUEST_INDEX = "AF";

    private static final String TOURNAMENT_INDEX = "ZEE";

    private static final String DATE_INDEX = "AD";

    private static final String STAGE_INDEX = "AC";

    private static final List<String> liveStageIds = Arrays.asList("12", "13", "36", "38");
    private static final List<String> prematchStageIds = Arrays.asList("1", "4", "43");

    public List<Match> parse(Document document) {
        return parse(document.body().text());
    }

    public List<Match> parse(String response) {
        List<String> rows = Arrays.asList(response.split(JS_ROW_END));
        String leagueId = null;
        List<Match> eventItems = new ArrayList<>();
        for (String s : rows) {
            List<String> row = Arrays.asList(s.split(JS_CELL_END));
            List<String> index = Arrays.asList(row.get(0).split(JS_INDEX));
            String indexName = null;
            if (!index.isEmpty() && StringUtils.hasLength(index.get(0))) {
                indexName = index.get(0);
            }

            if (LEAGUE_INDEX.equalsIgnoreCase(indexName)) {
                Map<String, String> tmp = new HashMap<>();
                for (int i = 0; i < row.size(); i++) {
                    List<String> rowParts = Arrays.asList(row.get(i).split(JS_INDEX));
                    if (rowParts.size() == 2) {
                        tmp.put(rowParts.get(0), rowParts.get(1));
                    }
                }
                leagueId = tmp.get(TOURNAMENT_INDEX);
            } else if (EVENT_INDEX.equalsIgnoreCase(indexName)) {

                Map<String, String> tmp = new HashMap<>();
                for (int i = 0; i < row.size(); i++) {
                    List<String> rowParts = Arrays.asList(row.get(i).split(JS_INDEX));
                    if (rowParts.size() == 2) {
                        tmp.put(rowParts.get(0), rowParts.get(1));
                    }
                }


                Match event = Match
                        .builder()
                        .homeTeam(Team.builder().name(tmp.get(HOME_INDEX)).build())
                        .awayTeam(Team.builder().name(tmp.get(GUEST_INDEX)).build())
                        .flashscoreId(tmp.get(EVENT_INDEX))
                        .flashscoreLeagueId(leagueId)
                        .matchStatus(
                                prematchStageIds.contains(tmp.get(STAGE_INDEX)) ? MatchStatus.PREMATCH :
                                        liveStageIds.contains(tmp.get(STAGE_INDEX)) ? MatchStatus.LIVE : MatchStatus.FINISHED

                        )
                        .date(LocalDateTime.ofInstant(
                                Instant.ofEpochSecond(Long.valueOf(tmp.get(DATE_INDEX))),
                                ZoneOffset.UTC
                        ))
                        .build();
                eventItems.add(event);
            }

        }
        return eventItems;
    }

}
