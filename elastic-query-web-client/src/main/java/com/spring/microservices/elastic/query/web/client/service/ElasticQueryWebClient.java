package com.spring.microservices.elastic.query.web.client.service;

import com.spring.microservices.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.spring.microservices.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;

import java.util.List;

public interface ElasticQueryWebClient {
    List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel);
}
