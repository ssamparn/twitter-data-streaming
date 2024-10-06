package com.spring.microservices.elastic.query.service.business.impl;

import com.spring.microservices.config.ElasticQueryServiceConfigData;
import com.spring.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.spring.microservices.elastic.query.client.service.ElasticQueryClient;
import com.spring.microservices.elastic.query.service.business.ElasticQueryService;
import com.spring.microservices.elastic.query.service.exception.ElasticQueryServiceException;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceWordCountResponseModel;
import com.spring.microservices.elastic.query.service.model.QueryType;
import com.spring.microservices.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;
    private final WebClient.Builder webClientBuilder;

    public TwitterElasticQueryService(ElasticQueryServiceConfigData elasticQueryServiceConfigData,
                                      ElasticQueryClient<TwitterIndexModel> elasticQueryClient,
                                      ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler,
                                      @Qualifier("webClientBuilder") WebClient.Builder webClientBuilder) {
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
        this.elasticQueryClient = elasticQueryClient;
        this.elasticQueryServiceResponseModelAssembler = elasticQueryServiceResponseModelAssembler;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents in elasticsearch");
        List<TwitterIndexModel> allIndexModels = elasticQueryClient.getAllIndexModels();
        return elasticQueryServiceResponseModelAssembler.toModels(allIndexModels);
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying elasticsearch by id {}", id);
        TwitterIndexModel indexModelById = elasticQueryClient.getIndexModelById(id);
        return elasticQueryServiceResponseModelAssembler.toModel(indexModelById);
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        LOG.info("Querying elasticsearch by text {}", text);
        List<TwitterIndexModel> indexModelsByText = elasticQueryClient.getIndexModelByText(text);
        return elasticQueryServiceResponseModelAssembler.toModels(indexModelsByText);
    }

    @Override
    public ElasticQueryServiceAnalyticsResponseModel getDocumentByTextWithAnalytics(String text) {
        LOG.info("Querying elasticsearch by text {}", text);
        List<TwitterIndexModel> indexModelsByText = elasticQueryClient.getIndexModelByText(text);
        List<ElasticQueryServiceResponseModel> elasticQueryServiceResponseModels = elasticQueryServiceResponseModelAssembler.toModels(indexModelsByText);
        Long wordCount = getWordCount(text);
        return ElasticQueryServiceAnalyticsResponseModel.create(elasticQueryServiceResponseModels, wordCount);
    }

    private Long getWordCount(String text) {
        if (QueryType.KAFKA_STATE_STORE.getType().equals(elasticQueryServiceConfigData.getWebClient().getQueryType())) {
            return getFromKafkaStateStore(text).getWordCount();
        }
        return 0L;
    }

    private ElasticQueryServiceWordCountResponseModel getFromKafkaStateStore(String text) {
        ElasticQueryServiceConfigData.Query queryFromKafkaStateStore = elasticQueryServiceConfigData.getQueryFromKafkaStateStore();
        return retrieveResponseModel(text, queryFromKafkaStateStore);
    }

    private ElasticQueryServiceWordCountResponseModel retrieveResponseModel(String text, ElasticQueryServiceConfigData.Query queryFromKafkaStateStore) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(queryFromKafkaStateStore.getMethod()))
                .uri(queryFromKafkaStateStore.getUri(), uriBuilder -> uriBuilder.build(text))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        s -> s.equals(HttpStatus.BAD_REQUEST),
                        clientResponse -> Mono.just(new ElasticQueryServiceException(clientResponse.statusCode().toString())))
                .onStatus(
                        s -> s.equals(HttpStatus.INTERNAL_SERVER_ERROR),
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().toString())))
                .bodyToMono(ElasticQueryServiceWordCountResponseModel.class)
                .log()
                .block();
    }

}
