package com.spring.microservices.elastic.query.client.service;

import com.spring.microservices.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticQueryClient<T extends IndexModel> {

    T getIndexModelById(String id);

    List<T> getIndexModelByTest(String text);

    List<T> getAllIndexModels();

}
