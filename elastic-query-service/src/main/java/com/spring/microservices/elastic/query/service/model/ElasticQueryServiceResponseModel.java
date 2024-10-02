package com.spring.microservices.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ElasticQueryServiceResponseModel {
    private String id;
    private Long userId;
    private String text;
    private ZonedDateTime createdAt;
}
