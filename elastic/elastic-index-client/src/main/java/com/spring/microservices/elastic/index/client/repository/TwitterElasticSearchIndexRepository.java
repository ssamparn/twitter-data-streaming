package com.spring.microservices.elastic.index.client.repository;

import com.spring.microservices.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterElasticSearchIndexRepository extends ElasticsearchRepository<TwitterIndexModel, String> {

}
