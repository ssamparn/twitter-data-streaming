package com.spring.microservices.elastic.query.web.client.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(staticName = "create")
public class ElasticQueryWebClientRequestModel {

    private String id;

    @NotEmpty
    private String text;
}
