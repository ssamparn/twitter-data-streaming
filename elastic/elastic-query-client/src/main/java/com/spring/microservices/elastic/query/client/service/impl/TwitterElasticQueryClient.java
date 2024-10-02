package com.spring.microservices.elastic.query.client.service.impl;

import com.spring.microservices.config.ElasticConfigData;
import com.spring.microservices.config.ElasticQueryConfigData;
import com.spring.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.spring.microservices.elastic.query.client.exception.ElasticQueryClientException;
import com.spring.microservices.elastic.query.client.service.ElasticQueryClient;
import com.spring.microservices.elastic.query.client.util.ElasticQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/* *
 * An alternative implementation of ElasticQueryClient
 * */
@Service
@ConditionalOnProperty(name = "elastic-query-config.is-repository", havingValue = "false")
public class TwitterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryClient.class);

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;

    public TwitterElasticQueryClient(ElasticConfigData elasticConfigData,
                                     ElasticQueryConfigData elasticQueryConfigData,
                                     ElasticsearchOperations elasticsearchOperations,
                                     ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticQueryUtil = elasticQueryUtil;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Query searchQueryById = elasticQueryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModel> twitterIndexModelSearchHit = elasticsearchOperations.searchOne(searchQueryById, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));

        if (twitterIndexModelSearchHit == null) {
            LOG.error("No document found at elasticsearch with id {}", id);
            throw new ElasticQueryClientException("No document found at elasticsearch with id " + id);
        }
        LOG.info("Document with id {} retrieved successfully", twitterIndexModelSearchHit.getId());
        return twitterIndexModelSearchHit.getContent();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByTest(String text) {
        Query searchQueryByFieldText = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), text);
        SearchHits<TwitterIndexModel> twitterIndexModelSearchHit = elasticsearchOperations.search(searchQueryByFieldText, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        LOG.info("{} number of documents with text {} retrieved successfully", twitterIndexModelSearchHit.getTotalHits(), text);
        return twitterIndexModelSearchHit.get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query searchQueryForAll = elasticQueryUtil.getSearchQueryForAll();
        SearchHits<TwitterIndexModel> twitterIndexModelSearchHit = elasticsearchOperations.search(searchQueryForAll, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        LOG.info("{} number of documents retrieved successfully", twitterIndexModelSearchHit.getTotalHits());
        return twitterIndexModelSearchHit.get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
