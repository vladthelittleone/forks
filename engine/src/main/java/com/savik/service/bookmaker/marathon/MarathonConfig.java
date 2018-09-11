package com.savik.service.bookmaker.marathon;

import com.savik.domain.SportType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "marathon")
@Getter
@Setter
public class MarathonConfig {

    String url;
    
    String marketPrefix;

    Map<SportType, String> prefixes;
    
    Map<SportType, String> prefixesLive;

    public String getMarketUrl() {
        return url + marketPrefix;
    }
    
    public String getSportUrl(SportType sportType) {
        return url + prefixes.get(sportType);
    }
    
    public String getLiveSportUrl(SportType sportType) {
        return url + prefixesLive.get(sportType);
    }

}
