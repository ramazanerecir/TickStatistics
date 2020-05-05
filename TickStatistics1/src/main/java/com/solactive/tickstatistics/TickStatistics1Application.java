package com.solactive.tickstatistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TickStatistics1Application {

    public static void main(String[] args) {
        SpringApplication.run(TickStatistics1Application.class, args);
    }
}
