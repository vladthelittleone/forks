package com.savik.service.bookmaker.matchbook;


import com.savik.domain.SportType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "matchbook")
@Getter
@Setter
public class MatchbookConfig {

    String url;

    String navigationPrefix;

    String eventsPrefix;

    Map<SportType, String> sportIds;
    
}
