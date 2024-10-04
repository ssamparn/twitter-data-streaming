package com.spring.microservices.elastic.query.web.client.config;

import com.spring.microservices.config.ElasticQueryWebClientConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
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
@LoadBalancerClient(name = "elastic-query-service", configuration = ElasticQueryServiceInstanceSupplierConfig.class)
public class WebClientConfig {

    private final ElasticQueryWebClientConfigData.WebClient elasticQueryWebClientConfigData;

    public WebClientConfig(ElasticQueryWebClientConfigData elasticQueryWebClientConfigData) {
        this.elasticQueryWebClientConfigData = elasticQueryWebClientConfigData.getWebClient();
    }

    /* *
     * @LoadBalanced annotation need to be used on the web client builder instead of web client directly
     * */
    @LoadBalanced
    @Bean("webClientBuilder")
    WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(elasticQueryWebClientConfigData.getBaseUrl())
                .defaultHeaders(httpHeaders())
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(elasticQueryWebClientConfigData.getMaxInMemorySize()));
    }

    private Consumer<HttpHeaders> httpHeaders() {
        return headers -> {
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        };
    }

    private HttpClient getHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, elasticQueryWebClientConfigData.getConnectTimeoutMs())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(elasticQueryWebClientConfigData.getReadTimeoutMs(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(elasticQueryWebClientConfigData.getWriteTimeoutMs(), TimeUnit.MILLISECONDS));
                });
    }
}
