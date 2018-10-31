package com.savik.config;

import com.savik.service.EngineService;
import com.savik.service.bookmaker.BookmakerAggregationService;
import com.savik.service.bookmaker.BookmakerService;
import com.savik.service.bookmaker.FastBookmaker;
import com.savik.service.bookmaker.SlowBookmaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class EngineConfiguration {
    
    @Autowired
    List<BookmakerService> bookmakerServices;
    
    @Bean
    public BookmakerAggregationService slowAggregationService() {
        return new BookmakerAggregationService(bookmakerServices.stream()
                .filter(b -> b.getClass().getAnnotation(SlowBookmaker.class) != null).collect(Collectors.toList()));
    }
    
    @Bean
    public BookmakerAggregationService fastAggregationService() {
        return new BookmakerAggregationService(bookmakerServices.stream()
                .filter(b -> b.getClass().getAnnotation(FastBookmaker.class) != null).collect(Collectors.toList()));
    }
    
    @Bean
    public EngineService slowBooksEngineService() {
        return new EngineService(slowAggregationService());
    }
    
    @Bean
    public EngineService fastBooksEngineService() {
        return new EngineService(fastAggregationService());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(
                new ClassPathResource("config/bookmakers/sbobet/sbobet-config.yml"),
                new ClassPathResource("config/bookmakers/pinnacle/pinnacle-config.yml"),
                new ClassPathResource("config/bookmakers/marathon/marathon-config.yml"),
                new ClassPathResource("config/bookmakers/matchbook/matchbook-config.yml")
        );
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }
}
