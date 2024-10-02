package com.spring.microservices.elastic.query.service.mapper;

import com.spring.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticModelToResponseModelMapper {

    public ElasticQueryServiceResponseModel toElasticQueryServiceResponseModel(TwitterIndexModel twitterIndexModel) {
        return getElasticQueryServiceResponseModel(twitterIndexModel);
    }

    public List<ElasticQueryServiceResponseModel> toElasticQueryServiceResponseModels(List<TwitterIndexModel> twitterIndexModel) {
        return twitterIndexModel.stream()
                .map(ElasticModelToResponseModelMapper::getElasticQueryServiceResponseModel)
                .collect(Collectors.toList());
    }

    private static ElasticQueryServiceResponseModel getElasticQueryServiceResponseModel(TwitterIndexModel twitterIndexModel) {
        return ElasticQueryServiceResponseModel.create(
                twitterIndexModel.getId(),
                twitterIndexModel.getUserId(),
                twitterIndexModel.getText(),
                twitterIndexModel.getCreatedAt()
        );
    }
}
