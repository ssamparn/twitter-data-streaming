package com.spring.microservices.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ElasticQueryServiceAnalyticsResponseModel {

    private List<ElasticQueryServiceResponseModel> elasticQueryResponseModels;
    private Long wordCount;
    
}
