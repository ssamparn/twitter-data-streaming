package com.spring.microservices.elastic.index.client.service;

import com.spring.microservices.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
