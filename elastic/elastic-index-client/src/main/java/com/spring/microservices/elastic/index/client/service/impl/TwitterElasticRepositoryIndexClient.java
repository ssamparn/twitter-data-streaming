package com.spring.microservices.elastic.index.client.service.impl;

import com.spring.microservices.elastic.index.client.repository.TwitterElasticSearchIndexRepository;
import com.spring.microservices.elastic.index.client.service.ElasticIndexClient;
import com.spring.microservices.elastic.model.index.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/* *
 * An alternative implementation of ElasticIndexClient
 * */
@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);

    private final TwitterElasticSearchIndexRepository twitterElasticSearchIndexRepository;

    public TwitterElasticRepositoryIndexClient(TwitterElasticSearchIndexRepository indexRepository) {
        this.twitterElasticSearchIndexRepository = indexRepository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> repositoryResponse =
                (List<TwitterIndexModel>) twitterElasticSearchIndexRepository.saveAll(documents);
        List<String> ids = repositoryResponse.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());
        LOG.info("Documents indexed successfully with type: {} and ids: {}", TwitterIndexModel.class.getName(), ids);
        return ids;
    }
}
