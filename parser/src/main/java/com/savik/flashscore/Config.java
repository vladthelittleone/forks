package com.savik.flashscore;


import com.savik.SportType;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

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

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(
                new ClassPathResource("flashscore-config.yml")
        );
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }
}
