package com.spoloborota.calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class SpringAsyncConfig {
    private static final int MAX_DAO_THREADS = 10;

        @Bean(name = "fixedThreadPoolExecutor")
        public Executor fixedThreadPoolExecutor() {
            return Executors.newFixedThreadPool(MAX_DAO_THREADS);
        }
}
