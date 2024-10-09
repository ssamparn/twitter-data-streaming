package com.spring.microservices.analytics.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.spring.microservices")
public class TwitterAnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterAnalyticsServiceApplication.class, args);
    }
}