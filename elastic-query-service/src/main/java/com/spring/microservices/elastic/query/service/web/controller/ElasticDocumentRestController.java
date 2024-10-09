package com.spring.microservices.elastic.query.service.web.controller;

import com.spring.microservices.elastic.query.service.business.ElasticQueryService;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.spring.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class ElasticDocumentRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentRestController.class);

    @Value("${server.port}")
    private String port;

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentRestController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @Operation(summary = "Get all elastic documents.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("")
    public @ResponseBody
    ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
        LOG.info("Elasticsearch returned all {} documents", response.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get elastic document by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel elasticQueryServiceResponseModel = elasticQueryService.getDocumentById(id);
        LOG.debug("Elasticsearch returned document with id {} on port {}", id, port);
        return ResponseEntity.ok(elasticQueryServiceResponseModel);
    }

    @Operation(summary = "Get elastic document by text.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/get-document-by-text")
    public @ResponseBody
    ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel) {
        List<ElasticQueryServiceResponseModel> elasticQueryServiceResponseModel = elasticQueryService.getDocumentByText(elasticQueryServiceRequestModel.getText());

        LOG.info("Elasticsearch returned {} of documents on port {}", elasticQueryServiceResponseModel, port);
        return new ResponseEntity<>(elasticQueryServiceResponseModel, HttpStatus.OK);
    }

    @Operation(summary = "Get elastic document by text with analytics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceAnalyticsResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/get-document-by-text-with-analytics")
    public @ResponseBody
    ResponseEntity<ElasticQueryServiceAnalyticsResponseModel> getDocumentByTextWithAnalytics(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel) {
        ElasticQueryServiceAnalyticsResponseModel elasticQueryServiceAnalyticsResponseModel = elasticQueryService.getDocumentByTextWithAnalytics(elasticQueryServiceRequestModel.getText());

        LOG.info("Elasticsearch returned {} of documents on port {}", elasticQueryServiceAnalyticsResponseModel, port);

        return new ResponseEntity<>(elasticQueryServiceAnalyticsResponseModel, HttpStatus.OK);
    }
}
