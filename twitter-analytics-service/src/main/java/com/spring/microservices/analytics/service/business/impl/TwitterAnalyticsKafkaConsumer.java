package com.spring.microservices.analytics.service.business.impl;

import com.spring.microservices.analytics.service.business.KafkaConsumer;
import com.spring.microservices.analytics.service.dataaccess.entity.TwitterAnalyticsEntity;
import com.spring.microservices.analytics.service.dataaccess.repository.TwitterAnalyticsRepository;
import com.spring.microservices.analytics.service.mapper.AvroToDbEntityModelMapper;
import com.spring.microservices.config.KafkaConfigData;
import com.spring.microservices.kafka.admin.client.KafkaAdminClient;
import com.spring.microservices.kafka.avro.model.TwitterAnalyticsAvroModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterAnalyticsKafkaConsumer implements KafkaConsumer<TwitterAnalyticsAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterAnalyticsKafkaConsumer.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfig;
    private final AvroToDbEntityModelMapper avroToDbEntityModelMapper;
    private final TwitterAnalyticsRepository twitterAnalyticsRepository;

    public TwitterAnalyticsKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                         KafkaAdminClient kafkaAdminClient,
                                         KafkaConfigData kafkaConfig,
                                         AvroToDbEntityModelMapper avroToDbEntityModelMapper,
                                         TwitterAnalyticsRepository twitterAnalyticsRepository) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfig = kafkaConfig;
        this.avroToDbEntityModelMapper = avroToDbEntityModelMapper;
        this.twitterAnalyticsRepository = twitterAnalyticsRepository;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfig.getTopicNamesToCreate().toArray());
        kafkaListenerEndpointRegistry.getListenerContainer("twitterAnalyticsTopicListener").start();
    }


    @Override
    @KafkaListener(id = "twitterAnalyticsTopicListener", topics = "${kafka-config.topic-name}", autoStartup = "false")
    public void receive(@Payload List<TwitterAnalyticsAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        LOG.info("{} number of message received with keys {}, partitions {} and offsets {}, " +
                        "sending it to database: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());

        List<TwitterAnalyticsEntity> twitterAnalyticsEntities = avroToDbEntityModelMapper.getEntityModel(messages);
        twitterAnalyticsRepository.batchPersist(twitterAnalyticsEntities);
        LOG.info("{} number of messaged send to database", twitterAnalyticsEntities.size());
    }
}
