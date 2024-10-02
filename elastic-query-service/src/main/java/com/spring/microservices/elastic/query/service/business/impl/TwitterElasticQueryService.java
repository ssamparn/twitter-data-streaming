package com.spring.microservices.elastic.query.service.business.impl;

import com.spring.microservices.config.ElasticQueryServiceConfigData;
import com.spring.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.spring.microservices.elastic.query.client.service.ElasticQueryClient;
import com.spring.microservices.elastic.query.service.business.ElasticQueryService;
import com.spring.microservices.elastic.query.service.mapper.ElasticModelToResponseModelMapper;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    private final ElasticModelToResponseModelMapper elasticModelToResponseModelMapper;

    public TwitterElasticQueryService(ElasticQueryServiceConfigData elasticQueryServiceConfigData,
                                      ElasticQueryClient<TwitterIndexModel> elasticQueryClient,
                                      ElasticModelToResponseModelMapper elasticModelToResponseModelMapper) {
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
        this.elasticQueryClient = elasticQueryClient;
        this.elasticModelToResponseModelMapper = elasticModelToResponseModelMapper;
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents in elasticsearch");
        List<TwitterIndexModel> allIndexModels = elasticQueryClient.getAllIndexModels();
        return elasticModelToResponseModelMapper.toElasticQueryServiceResponseModels(allIndexModels);
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying elasticsearch by id {}", id);
        TwitterIndexModel indexModelById = elasticQueryClient.getIndexModelById(id);
        return elasticModelToResponseModelMapper.toElasticQueryServiceResponseModel(indexModelById);
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        LOG.info("Querying elasticsearch by text {}", text);
        List<TwitterIndexModel> indexModelsByText = elasticQueryClient.getIndexModelByText(text);
        return elasticModelToResponseModelMapper.toElasticQueryServiceResponseModels(indexModelsByText);
    }
}
