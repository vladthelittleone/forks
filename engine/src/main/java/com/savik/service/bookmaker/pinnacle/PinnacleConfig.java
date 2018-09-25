package com.savik.service.bookmaker.pinnacle;


import com.savik.domain.SportType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "pinnacle")
@Getter
@Setter
class PinnacleConfig {

    String url;

    String apiV1Version;

    String apiV2Version;

    String fixtures;

    String odds;

    Map<SportType, String> sportIds;

    @Autowired
    Environment environment;

    public String getFixtureUrl(SportType sportType) {
        if (!sportIds.containsKey(sportType)) {
            throw new IllegalArgumentException("Unknown sport type: " + sportType);
        }
        return url + apiV1Version + fixtures + "?sportId=" + sportIds.get(sportType);
    }

    public String getOddsUrl(SportType sportType, Long since) {
        if (!sportIds.containsKey(sportType)) {
            throw new IllegalArgumentException("Unknown sport type: " + sportType);
        }
        return url + apiV1Version + odds + "sportId=" + sportIds.get(sportType) +
                (since == null ? "" : "&since=" + since);
    }

    public String getBase64Authentications() {
        String login = environment.getProperty("pinnacle.login");
        String password = environment.getProperty("pinnacle.password");
        String auth = String.format("%s:%s", login, password);
        return new String(Base64.encodeBase64(auth.getBytes()));
    }
}
