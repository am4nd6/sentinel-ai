package com.sentinelai.pipeline.infrastructure;

import com.sentinelai.pipeline.domain.EventConsumerReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventConsumerReceiptRepository extends JpaRepository<EventConsumerReceipt, EventConsumerReceipt.Key> {}
