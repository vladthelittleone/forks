package com.savik.service.bookmaker.sbobet;


import com.savik.domain.SportType;
import com.savik.model.BookmakerMatchWrapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sbobet")
@Getter
@Setter
public class SbobetConfig {

    String url;

    Map<SportType, String> prefixes;

    public String getSportUrl(SportType sportType, int daysFromToday) {
        String daysSbobetFormat = LocalDateTime.now().plusDays(daysFromToday).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (daysFromToday == 0) {
            daysSbobetFormat = "";
        }
        return url + prefixes.get(sportType) + "/" + daysSbobetFormat;
    }

    public String getMatchUrl(String sbobetMatchId, BookmakerMatchWrapper bookmakerMatchWrapper) {
        String newLeagueName = bookmakerMatchWrapper.getBookmakerLeague().getName().toLowerCase().replaceAll("\\s+", "-").replace("'","-");
        String newHomeName = bookmakerMatchWrapper.getHomeTeam().getName().toLowerCase().replaceAll("\\s+", "-").replace("'","-");
        String newGuestName = bookmakerMatchWrapper.getAwayTeam().getName().toLowerCase().replaceAll("\\s+", "-").replace("'","-");
        return url + prefixes.get(bookmakerMatchWrapper.getMatch().getSportType()) + "/" + newLeagueName + "/" + sbobetMatchId + "/" + newHomeName + "-vs-" + newGuestName;
    }
}
