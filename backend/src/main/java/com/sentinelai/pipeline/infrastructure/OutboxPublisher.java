package com.sentinelai.pipeline.infrastructure;

import com.sentinelai.pipeline.domain.EventOutbox;
import java.time.Instant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "sentinel.pipeline.kafka-enabled", havingValue = "true")
public class OutboxPublisher {
    private final EventOutboxRepository outbox;
    private final KafkaTemplate<Object, Object> kafka;
    public OutboxPublisher(EventOutboxRepository outbox, KafkaTemplate<Object, Object> kafka) { this.outbox = outbox; this.kafka = kafka; }
    @Scheduled(fixedDelayString = "${sentinel.pipeline.outbox-poll-ms:1000}")
    @Transactional
    public void publishPending() {
        outbox.findTop20ByStatusAndAvailableAtLessThanEqualOrderByCreatedAtAsc("PENDING", Instant.now())
            .forEach(event -> publish(event));
    }
    private void publish(EventOutbox event) {
        event.markPublishing();
        kafka.send(event.getTopic(), event.getAggregateId().toString(), event.getPayload()).whenComplete((ignored, error) -> {
            if (error == null) event.markPublished(); else event.markRetry(error.getMessage());
            outbox.save(event);
        });
    }
}
