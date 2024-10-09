package com.spring.microservices.analytics.service.web;

import com.spring.microservices.analytics.service.business.AnalyticsService;
import com.spring.microservices.analytics.service.model.AnalyticsResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class TwitterAnalyticsRestController {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterAnalyticsRestController.class);

    private final AnalyticsService analyticsService;

    public TwitterAnalyticsRestController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/get-word-count-by-word/{word}")
    @Operation(summary = "Get analytics by word.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = AnalyticsResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    public @ResponseBody
    ResponseEntity<AnalyticsResponseModel> getWordCountByWord(@PathVariable @NotEmpty String word) {
        Optional<AnalyticsResponseModel> response = analyticsService.getWordAnalytics(word);
        if (response.isPresent()) {
            LOG.info("Analytics data returned with id {}", response.get().getId());
            return ResponseEntity.ok(response.get());
        }
        return ResponseEntity.ok(AnalyticsResponseModel.create(null, null, 0L));
    }
}
