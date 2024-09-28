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
                .authorizeHttpRequests((authz) -> authz
                    .requestMatchers(
                            "/config-client/twitter_to_kafka",
                            "/actuator/**",
                            "/encrypt/**",
                            "/decrypt/**")
                        .permitAll()
                        .anyRequest().authenticated()
                ).httpBasic(withDefaults());
        return http.build();
    }
}
