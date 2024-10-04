package com.spring.microservices.elastic.query.web.client.web.controller;

import com.spring.microservices.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.spring.microservices.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.spring.microservices.elastic.query.web.client.service.ElasticQueryWebClient;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryRestController {

    private static final Logger LOG = LoggerFactory.getLogger(QueryRestController.class);

    private final ElasticQueryWebClient elasticQueryWebClient;

    public QueryRestController(ElasticQueryWebClient elasticQueryWebClient) {
        this.elasticQueryWebClient = elasticQueryWebClient;
    }

    @PostMapping("/query-by-text")
    public ResponseEntity<List<ElasticQueryWebClientResponseModel>> queryByText(@Valid @RequestBody ElasticQueryWebClientRequestModel elasticQueryWebClientRequestModel) {
        LOG.info("Querying with text {}", elasticQueryWebClientRequestModel.getText());

        List<ElasticQueryWebClientResponseModel> responseModels = elasticQueryWebClient.getDataByText(elasticQueryWebClientRequestModel);

        return new ResponseEntity<>(responseModels, HttpStatus.OK);
    }
}
