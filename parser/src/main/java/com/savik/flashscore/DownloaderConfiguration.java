package com.savik.flashscore;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@Getter
public class DownloaderConfiguration {

    @Value("${url.sportMatches}")
    private String sportMatches;

    @Value("${fsign}")
    private String fsign;

}
