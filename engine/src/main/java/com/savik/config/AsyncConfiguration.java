package com.savik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfiguration {

    private final int POOL_SIZE = 10;

    
    @Bean
    public Executor slowBookmakersExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("slow-bookmakers-task-pool-");
        threadPoolTaskExecutor.setMaxPoolSize(POOL_SIZE);
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setDaemon(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
    
    @Bean
    public Executor fastBookmakersExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("fast-bookmakers-task-pool-");
        threadPoolTaskExecutor.setMaxPoolSize(POOL_SIZE);
        threadPoolTaskExecutor.setCorePoolSize(POOL_SIZE);
        threadPoolTaskExecutor.setDaemon(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean
    public Executor arbPrechecker() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("arb-prechecker-");
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setDaemon(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
