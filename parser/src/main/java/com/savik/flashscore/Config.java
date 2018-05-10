package com.savik.flashscore;


import com.savik.SportType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class Config {

    @Bean
    public List<SportConfig> sportConfigs() {
        return Arrays.asList(
                new SportConfig(SportType.FOOTBALL, "1"),
                new SportConfig(SportType.HOCKEY, "4")
        );
    }
}
