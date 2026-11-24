package com.sentinelai.pipeline.infrastructure;

import com.sentinelai.pipeline.application.PersistentIdempotentEventProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "sentinel.pipeline.kafka-enabled", havingValue = "true")
public class EventConsumer {
    private final PersistentIdempotentEventProcessor processor;
    public EventConsumer(PersistentIdempotentEventProcessor processor) { this.processor = processor; }
    @KafkaListener(topics = "sentinel.events.v1", groupId = "sentinel-correlator", containerFactory = "pipelineKafkaFactory")
    public void consume(String payload, org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
        String key = record.key() == null ? payload : record.key();
        processor.process("sentinel-correlator", key, () -> { if (payload == null || payload.isBlank()) throw new IllegalArgumentException("empty event"); });
    }
}
