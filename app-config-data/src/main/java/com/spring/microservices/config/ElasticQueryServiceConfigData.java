package com.spring.microservices.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic-query-service")
public class ElasticQueryServiceConfigData {

    private String version;
    private WebClient webClient;
    private Query queryFromKafkaStateStore;
    private Query queryFromAnalyticsDatabase;

    @Data
    public static class WebClient {
        private Integer connectTimeoutMs;
        private Integer readTimeoutMs;
        private Integer writeTimeoutMs;
        private Integer maxInMemorySize;
        private String queryType;
    }

    @Data
    public static class Query {
        private String method;
        private String uri;
    }

}
