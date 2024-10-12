package com.spring.microservices.api.gateway.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class AnalyticsDataFallbackModel {
    private Long wordCount;
}
