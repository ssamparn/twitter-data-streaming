package com.spring.microservices.elastic.query.service.business;

import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;

import java.util.List;

public interface ElasticQueryService {
    List<ElasticQueryServiceResponseModel> getAllDocuments();
    ElasticQueryServiceResponseModel getDocumentById(String id);
    List<ElasticQueryServiceResponseModel> getDocumentByText(String text);
}
