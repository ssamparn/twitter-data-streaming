package com.spring.microservices.elastic.query.web.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ElasticQueryWebClientAnalyticsResponseModel {
    private List<ElasticQueryWebClientResponseModel> elasticQueryResponseModels;
    private Long wordCount;
}
