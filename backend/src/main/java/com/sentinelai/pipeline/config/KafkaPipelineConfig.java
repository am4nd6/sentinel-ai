package com.sentinelai.pipeline.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
@ConditionalOnProperty(name = "sentinel.pipeline.kafka-enabled", havingValue = "true")
public class KafkaPipelineConfig {
    @Bean
    DefaultErrorHandler pipelineErrorHandler(KafkaTemplate<Object, Object> template) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
            (record, error) -> new TopicPartition(record.topic() + ".dlq", record.partition()));
        ExponentialBackOff backoff = new ExponentialBackOff(250L, 2.0);
        backoff.setMaxInterval(5000L); backoff.setMaxElapsedTime(15000L);
        return new DefaultErrorHandler(recoverer, backoff);
    }
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> pipelineKafkaFactory(
            ConsumerFactory<String, String> consumers, DefaultErrorHandler errors) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumers); factory.setCommonErrorHandler(errors);
        factory.setConcurrency(1); factory.getContainerProperties().setPollTimeout(1000L);
        return factory;
    }
}
