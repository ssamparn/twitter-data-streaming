package com.spring.microservices.kafka.streams.service;

import com.spring.microservices.kafka.streams.service.initialize.StreamsInitializer;
import com.spring.microservices.kafka.streams.service.runner.StreamsRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = "com.spring.microservices")
public class KafkaStreamsServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsServiceApplication.class);

    private final StreamsRunner<String, Long> streamsRunner;

    private final StreamsInitializer streamsInitializer;

    public KafkaStreamsServiceApplication(StreamsRunner<String, Long> streamsRunner,
                                          StreamsInitializer streamsInitializer) {
        this.streamsRunner = streamsRunner;
        this.streamsInitializer = streamsInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamsServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        LOG.info("Kafka Streams Application starts...");
        streamsInitializer.init();
        streamsRunner.start();
    }
}
