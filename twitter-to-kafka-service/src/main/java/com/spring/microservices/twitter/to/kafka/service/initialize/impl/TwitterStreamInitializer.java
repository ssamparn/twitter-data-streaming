package com.spring.microservices.twitter.to.kafka.service.initialize.impl;

import com.spring.microservices.config.KafkaConfigData;
import com.spring.microservices.kafka.admin.client.KafkaAdminClient;
import com.spring.microservices.twitter.to.kafka.service.initialize.StreamInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component
public class TwitterStreamInitializer implements StreamInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterStreamInitializer.class);

    private final KafkaConfigData kafkaConfigData;

    private final KafkaAdminClient kafkaAdminClient;

    public TwitterStreamInitializer(KafkaConfigData configData, KafkaAdminClient adminClient) {
        this.kafkaConfigData = configData;
        this.kafkaAdminClient = adminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
