package com.spring.microservices.elastic.query.web.client.service.impl;

import com.spring.microservices.config.ElasticQueryWebClientConfigData;
import com.spring.microservices.elastic.query.web.client.exception.ElasticQueryWebClientException;
import com.spring.microservices.elastic.query.web.client.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.spring.microservices.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.spring.microservices.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.spring.microservices.elastic.query.web.client.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.spring.microservices.common.constants.Constants.CORRELATION_ID_HEADER;
import static com.spring.microservices.common.constants.Constants.CORRELATION_ID_KEY;

@Service
public class TwitterElasticQueryWebClient implements ElasticQueryWebClient {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryWebClient.class);

    private final WebClient.Builder webClientBuilder;
    private final ElasticQueryWebClientConfigData elasticQueryWebClientConfigData;

    public TwitterElasticQueryWebClient(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                                        ElasticQueryWebClientConfigData elasticQueryWebClientConfigData) {
        this.webClientBuilder = webClientBuilder;
        this.elasticQueryWebClientConfigData = elasticQueryWebClientConfigData;
    }

    @Override
    public List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel) {
        LOG.info("Querying by text {}", requestModel.getText());
        return getWebClient(elasticQueryWebClientConfigData.getQueryByText().getUri() ,requestModel)
                .bodyToFlux(ElasticQueryWebClientResponseModel.class)
                .collectList()
                .block();
    }

    @Override
    public ElasticQueryWebClientAnalyticsResponseModel getDataByTextWithAnalytics(ElasticQueryWebClientRequestModel requestModel) {
        LOG.info("Querying by text {}", requestModel.getText());
        return getWebClient(elasticQueryWebClientConfigData.getQueryByTextWithAnalytics().getUri(), requestModel)
                .bodyToMono(ElasticQueryWebClientAnalyticsResponseModel.class)
                .block();
    }

    private WebClient.ResponseSpec getWebClient(String uri, ElasticQueryWebClientRequestModel requestModel) {
        return webClientBuilder.build()
                .method(HttpMethod.valueOf(elasticQueryWebClientConfigData.getQueryByText().getMethod()))
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(CORRELATION_ID_HEADER, MDC.get(CORRELATION_ID_KEY))
                .body(BodyInserters.fromPublisher(Mono.just(requestModel), createParameterizedTypeReference()))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated!")))
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.BAD_REQUEST),
                        clientResponse -> Mono.just(
                                new ElasticQueryWebClientException(clientResponse.statusCode().toString())))
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.INTERNAL_SERVER_ERROR),
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().toString())));
    }

    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
