package com.spring.microservices.twitter.to.kafka.service;

import com.spring.microservices.twitter.to.kafka.service.initialize.StreamInitializer;
import com.spring.microservices.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.spring.microservices")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;
    @Autowired
    public TwitterToKafkaServiceApplication(StreamRunner streamRunner, StreamInitializer initializer) {
        this.streamRunner = streamRunner;
        this.streamInitializer = initializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        LOG.info("App starts...");
        streamInitializer.init();
        streamRunner.start();
    }
}
