package com.savik;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties
public class EngineApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EngineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        byte[] jsonData = Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("config/bookmakers/pinnacle/football_soccer.json").toURI()));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        Map<String, ArrayList<Map>> map = objectMapper.readValue(jsonData, Map.class);
        ArrayList<Map> leagues = map.get("leagues");

        for (Map league : leagues) {
            System.out.println("INSERT INTO PUBLIC.BOOKMAKER_LEAGUE (BOOKMAKER_TYPE, ITEM_FLASHSCORE_ID, BOOKMAKER_ID, NAME)  VALUES('PINNACLE', '', '" +
                    league.get("id") + "', '"+league.get("name")+"');");
        }

        String a = "";


    }

}