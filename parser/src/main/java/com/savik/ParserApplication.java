package com.savik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableScheduling
//@EnableDiscoveryClient
public class ParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
    }

}