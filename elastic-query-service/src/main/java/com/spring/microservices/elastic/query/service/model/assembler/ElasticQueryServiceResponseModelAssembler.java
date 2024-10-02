package com.spring.microservices.elastic.query.service.model.assembler;

import com.spring.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.spring.microservices.elastic.query.service.mapper.ElasticModelToResponseModelMapper;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.spring.microservices.elastic.query.service.web.controller.ElasticDocumentRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ElasticQueryServiceResponseModelAssembler
        extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponseModel> {

    private final ElasticModelToResponseModelMapper elasticModelToResponseModelMapper;

    public ElasticQueryServiceResponseModelAssembler(ElasticModelToResponseModelMapper elasticModelToResponseModelMapper) {
        super(ElasticDocumentRestController.class, ElasticQueryServiceResponseModel.class);
        this.elasticModelToResponseModelMapper = elasticModelToResponseModelMapper;
    }

    @Override
    public ElasticQueryServiceResponseModel toModel(TwitterIndexModel twitterIndexModel) {
        ElasticQueryServiceResponseModel responseModel = elasticModelToResponseModelMapper.toElasticQueryServiceResponseModel(twitterIndexModel);
        responseModel.add(
                linkTo(methodOn(ElasticDocumentRestController.class)
                        .getDocumentById((twitterIndexModel.getId())))
                        .withSelfRel());
        responseModel.add(
                linkTo(ElasticDocumentRestController.class)
                        .withRel("documents"));
        return responseModel;
    }

    public List<ElasticQueryServiceResponseModel> toModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream().map(this::toModel).collect(Collectors.toList());
    }


}

