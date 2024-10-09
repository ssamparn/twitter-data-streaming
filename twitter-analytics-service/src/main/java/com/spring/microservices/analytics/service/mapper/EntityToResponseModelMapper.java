package com.spring.microservices.analytics.service.mapper;

import com.spring.microservices.analytics.service.dataaccess.entity.TwitterAnalyticsEntity;
import com.spring.microservices.analytics.service.model.AnalyticsResponseModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EntityToResponseModelMapper {

    public Optional<AnalyticsResponseModel> getResponseModel(TwitterAnalyticsEntity twitterAnalyticsEntity) {
        if (twitterAnalyticsEntity == null) return Optional.empty();
        return Optional.ofNullable(
                AnalyticsResponseModel.create(
                        twitterAnalyticsEntity.getId(),
                        twitterAnalyticsEntity.getWord(),
                        twitterAnalyticsEntity.getWordCount()
                )
        );
    }
}
