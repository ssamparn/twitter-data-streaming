package com.spring.microservices.api.gateway.service.config;

import com.spring.microservices.config.ApiGatewayConfigData;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ApiGatewayConfig {

    private final ApiGatewayConfigData apiGatewayConfigData;

    public ApiGatewayConfig(ApiGatewayConfigData apiGatewayConfigData) {
        this.apiGatewayConfigData = apiGatewayConfigData;
    }

    @Bean
    Customizer<ReactiveResilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer() {
        return reactiveResilience4JCircuitBreakerFactory ->
                reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofMillis(apiGatewayConfigData.getTimeoutMs()))
                                .build())
                        .circuitBreakerConfig(CircuitBreakerConfig.custom()
                                .failureRateThreshold(apiGatewayConfigData.getFailureRateThreshold())
                                .slowCallRateThreshold(apiGatewayConfigData.getSlowCallRateThreshold())
                                .slowCallDurationThreshold(Duration.ofMillis(apiGatewayConfigData.getSlowCallDurationThreshold()))
                                .permittedNumberOfCallsInHalfOpenState(apiGatewayConfigData.getPermittedNumOfCallsInHalfOpenState())
                                .slidingWindowSize(apiGatewayConfigData.getSlidingWindowSize())
                                .minimumNumberOfCalls(apiGatewayConfigData.getMinNumberOfCalls())
                                .waitDurationInOpenState(Duration.ofMillis(apiGatewayConfigData.getWaitDurationInOpenState()))
                                .build())
                        .build());
    }
}
