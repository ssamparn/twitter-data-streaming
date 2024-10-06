package com.spring.microservices.kafka.streams.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class KafkaStreamsResponseModel {
    private String word;
    private Long wordCount;
}
