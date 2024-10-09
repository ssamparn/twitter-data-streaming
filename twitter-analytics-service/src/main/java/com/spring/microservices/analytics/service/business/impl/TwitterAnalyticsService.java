package com.spring.microservices.analytics.service.business.impl;

import com.spring.microservices.analytics.service.business.AnalyticsService;
import com.spring.microservices.analytics.service.dataaccess.entity.TwitterAnalyticsEntity;
import com.spring.microservices.analytics.service.dataaccess.repository.TwitterAnalyticsRepository;
import com.spring.microservices.analytics.service.mapper.EntityToResponseModelMapper;
import com.spring.microservices.analytics.service.model.AnalyticsResponseModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TwitterAnalyticsService implements AnalyticsService {

    private final TwitterAnalyticsRepository analyticsRepository;
    private final EntityToResponseModelMapper entityToResponseModelMapper;

    public TwitterAnalyticsService(TwitterAnalyticsRepository analyticsRepository,
                                   EntityToResponseModelMapper entityToResponseModelMapper) {
        this.analyticsRepository = analyticsRepository;
        this.entityToResponseModelMapper = entityToResponseModelMapper;
    }

    @Override
    public Optional<AnalyticsResponseModel> getWordAnalytics(String word) {
        TwitterAnalyticsEntity analyticsEntity = analyticsRepository
                .getAnalyticsEntitiesByWord(word, PageRequest.of(0,1))
                .stream()
                .findFirst()
                .orElse(null);

        return entityToResponseModelMapper.getResponseModel(analyticsEntity);
    }
}
