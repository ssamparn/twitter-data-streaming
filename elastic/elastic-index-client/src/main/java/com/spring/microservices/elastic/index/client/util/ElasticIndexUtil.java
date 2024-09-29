package com.spring.microservices.elastic.index.client.util;

import com.spring.microservices.elastic.model.index.IndexModel;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/* *
 * Generic upper bound is used: Relax the restriction & allow subtypes
 * */
@Component
public class ElasticIndexUtil<T extends IndexModel> {

    /* *
     * Converts list of twitter model object to a list of elastic index query, to be able to send the query to elastic search
     * */
    public List<IndexQuery> getIndexQueries(List<T> documents) {
        return documents.stream()
                .map(document -> new IndexQueryBuilder()
                        .withId(document.getId())
                        .withObject(document)
                        .build()
                ).collect(Collectors.toList());
    }
}