package com.spring.microservices.config.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                    .requestMatchers(
                            "/config-client/twitter_to_kafka",
                            "/config-client/kafka_to_elastic",
                            "/config-client/elastic_query",
                            "/config-client/elastic_query_web",
                            "/actuator/**",
                            "/encrypt/**",
                            "/decrypt/**")
                        .permitAll()
                        .anyRequest().authenticated()
                ).httpBasic(withDefaults());
        return http.build();
    }
}
