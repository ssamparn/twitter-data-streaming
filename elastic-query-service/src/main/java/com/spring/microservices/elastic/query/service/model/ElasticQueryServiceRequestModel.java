package com.spring.microservices.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ElasticQueryServiceRequestModel {
    private String id;
    private String text;
}
