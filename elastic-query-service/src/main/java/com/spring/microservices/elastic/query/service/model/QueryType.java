package com.spring.microservices.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueryType {

    KAFKA_STATE_STORE("KAFKA_STATE_STORE"),
    ANALYTICS_DATABASE("ANALYTICS_DATABASE");

    private String type;
}
