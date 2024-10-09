package com.spring.microservices.analytics.service.mapper;

import com.spring.microservices.analytics.service.dataaccess.entity.TwitterAnalyticsEntity;
import com.spring.microservices.kafka.avro.model.TwitterAnalyticsAvroModel;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class AvroToDbEntityModelMapper {

    private final IdGenerator idGenerator;

    public AvroToDbEntityModelMapper(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public List<TwitterAnalyticsEntity> getEntityModel(List<TwitterAnalyticsAvroModel> avroModels) {
        return avroModels.stream()
                .map(avroModel -> new TwitterAnalyticsEntity(
                        idGenerator.generateId(),
                        avroModel.getWord(),
                        avroModel.getWordCount(),
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(avroModel.getCreatedAt()), ZoneOffset.UTC)))
                .collect(toList());
    }
}