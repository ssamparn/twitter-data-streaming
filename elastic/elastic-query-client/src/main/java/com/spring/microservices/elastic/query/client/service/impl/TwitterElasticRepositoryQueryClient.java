package com.spring.microservices.elastic.query.client.service.impl;

import com.spring.microservices.common.util.CollectionsUtil;
import com.spring.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.spring.microservices.elastic.query.client.exception.ElasticQueryClientException;
import com.spring.microservices.elastic.query.client.repository.TwitterElasticsearchQueryRepository;
import com.spring.microservices.elastic.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/* *
 * An alternative implementation of ElasticQueryClient
 * */
@Service
@ConditionalOnProperty(name = "elastic-query-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryQueryClient.class);

    private final TwitterElasticsearchQueryRepository twitterElasticsearchQueryRepository;

    public TwitterElasticRepositoryQueryClient(TwitterElasticsearchQueryRepository twitterElasticsearchQueryRepository) {
        this.twitterElasticsearchQueryRepository = twitterElasticsearchQueryRepository;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Optional<TwitterIndexModel> twitterIndexModelByIdOptional = twitterElasticsearchQueryRepository.findById(id);
        LOG.info("Document with id {} retrieved successfully", twitterIndexModelByIdOptional.orElseThrow(() ->
                        new ElasticQueryClientException("No document found at elasticsearch with id " + id)).getId());
        return twitterIndexModelByIdOptional.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        List<TwitterIndexModel> twitterIndexModels = twitterElasticsearchQueryRepository.findByText(text);
        LOG.info("{} number of documents with text {} retrieved successfully", twitterIndexModels.size(), text);
        return twitterIndexModels;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        List<TwitterIndexModel> twitterIndexModels =
                CollectionsUtil.getInstance().getListFromIterable(twitterElasticsearchQueryRepository.findAll());
        LOG.info("{} number of documents retrieved successfully", twitterIndexModels.size());
        return twitterIndexModels;
    }
}
