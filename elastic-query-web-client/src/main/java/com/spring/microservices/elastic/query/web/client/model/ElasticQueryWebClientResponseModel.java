package com.spring.microservices.elastic.query.web.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(staticName = "create")
public class ElasticQueryWebClientResponseModel {

    private String id;
    private Long userId;
    private String text;
    private ZonedDateTime createdAt;
}
