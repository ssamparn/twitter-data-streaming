package com.spring.microservices.analytics.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class AnalyticsResponseModel {
    private UUID id;
    private String word;
    private long wordCount;
}
