package com.spring.microservices.analytics.service.business;

import com.spring.microservices.analytics.service.model.AnalyticsResponseModel;

import java.util.Optional;

public interface AnalyticsService {

    Optional<AnalyticsResponseModel> getWordAnalytics(String word);
}
