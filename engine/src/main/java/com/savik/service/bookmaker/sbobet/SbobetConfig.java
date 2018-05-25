package com.savik.service.bookmaker.sbobet;


import com.savik.domain.SportType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sbobet")
@Getter
@Setter
public class SbobetConfig {

    String url;

    Map<SportType,String> prefixes;
}
