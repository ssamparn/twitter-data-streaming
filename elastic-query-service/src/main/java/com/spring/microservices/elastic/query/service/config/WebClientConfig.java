package com.spring.microservices.elastic.query.service.config;

import com.spring.microservices.config.ElasticQueryServiceConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Configuration
public class WebClientConfig {

    private final ElasticQueryServiceConfigData.WebClient elasticQueryServiceConfigData;

    public WebClientConfig(ElasticQueryServiceConfigData queryServiceConfigData) {
        this.elasticQueryServiceConfigData = queryServiceConfigData.getWebClient();
    }

    @LoadBalanced
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeaders(httpHeaders())
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(elasticQueryServiceConfigData.getMaxInMemorySize()));
    }

    private Consumer<HttpHeaders> httpHeaders() {
        return headers -> {
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        };
    }

    private HttpClient getHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, elasticQueryServiceConfigData.getConnectTimeoutMs())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(elasticQueryServiceConfigData.getReadTimeoutMs(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(elasticQueryServiceConfigData.getWriteTimeoutMs(), TimeUnit.MILLISECONDS));
                });
    }
}
