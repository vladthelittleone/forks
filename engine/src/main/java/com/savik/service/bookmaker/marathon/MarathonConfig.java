package com.savik.service.bookmaker.marathon;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "marathon")
@Getter
@Setter
public class MarathonConfig {

    String url;

}
