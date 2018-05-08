package com.savik;

import com.savik.repository.FutureMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    FutureMatchRepository futureMatchRepository;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {


            FutureMatch futureMatch = FutureMatch.builder()
                    .matchStatus(MatchStatus.FINISHED)
                    .flashscoreId("test")
                    .sportType(SportType.FOOTBALL)
                    .build();

            futureMatchRepository.save(futureMatch);

        };
    }

}