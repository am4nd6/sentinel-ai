package com.sentinelai.pipeline.application;

import com.sentinelai.pipeline.domain.EventConsumerReceipt;
import com.sentinelai.pipeline.infrastructure.EventConsumerReceiptRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersistentIdempotentEventProcessor {
    private final EventConsumerReceiptRepository receipts;
    private final PipelineMetrics metrics;
    public PersistentIdempotentEventProcessor(EventConsumerReceiptRepository receipts, PipelineMetrics metrics) {
        this.receipts = receipts; this.metrics = metrics;
    }
    @Transactional
    public boolean process(String consumer, String eventKey, Runnable handler) {
        if (receipts.existsById(new EventConsumerReceipt.Key(consumer, eventKey))) { metrics.duplicate(); return false; }
        try {
            receipts.saveAndFlush(new EventConsumerReceipt(consumer, eventKey));
            handler.run(); metrics.processed(); return true;
        } catch (DataIntegrityViolationException duplicate) {
            metrics.duplicate(); return false;
        } catch (RuntimeException failure) {
            metrics.failure(); throw failure;
        }
    }
}
