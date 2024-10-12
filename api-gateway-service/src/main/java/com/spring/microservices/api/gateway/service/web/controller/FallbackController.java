package com.spring.microservices.api.gateway.service.web.controller;

import com.spring.microservices.api.gateway.service.model.AnalyticsDataFallbackModel;
import com.spring.microservices.api.gateway.service.model.QueryServiceFallbackModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger LOG = LoggerFactory.getLogger(FallbackController.class);

    @Value("${server.port}")
    private String port;

    @PostMapping("/query-fallback")
    public ResponseEntity<QueryServiceFallbackModel> queryServiceFallback() {
        LOG.info("Returning fallback result for elastic-query-service! on port {}", port);
        return ResponseEntity.ok(QueryServiceFallbackModel
                .create("Fallback result for elastic-query-service!"));
    }

    @PostMapping("/analytics-fallback")
    public ResponseEntity<AnalyticsDataFallbackModel> analyticsServiceFallback() {
        LOG.info("Returning fallback result for analytics-service! on port {}", port);
        return ResponseEntity.ok(AnalyticsDataFallbackModel.create(0L));
    }


    @PostMapping("/streams-fallback")
    public ResponseEntity<AnalyticsDataFallbackModel> streamsServiceFallback() {
        LOG.info("Returning fallback result for kafka-streams-service! on port {}", port);
        return ResponseEntity.ok(AnalyticsDataFallbackModel.create(0L));
    }
}
