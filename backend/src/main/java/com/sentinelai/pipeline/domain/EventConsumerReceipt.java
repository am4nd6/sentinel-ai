package com.sentinelai.pipeline.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "event_consumer_receipt")
@IdClass(EventConsumerReceipt.Key.class)
public class EventConsumerReceipt {
    @Id private String consumerName;
    @Id private String eventKey;
    private Instant processedAt;
    protected EventConsumerReceipt() {}
    public EventConsumerReceipt(String consumerName, String eventKey) {
        this.consumerName = consumerName; this.eventKey = eventKey; this.processedAt = Instant.now();
    }
    public record Key(String consumerName, String eventKey) implements Serializable {}
}
