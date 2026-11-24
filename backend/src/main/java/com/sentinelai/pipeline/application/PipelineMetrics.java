package com.sentinelai.pipeline.application;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class PipelineMetrics {
    private final AtomicLong processed = new AtomicLong();
    private final AtomicLong duplicates = new AtomicLong();
    private final AtomicLong failures = new AtomicLong();
    private final AtomicLong deadLetters = new AtomicLong();
    public void processed() { processed.incrementAndGet(); }
    public void duplicate() { duplicates.incrementAndGet(); }
    public void failure() { failures.incrementAndGet(); }
    public void deadLetter() { deadLetters.incrementAndGet(); }
    public long processedCount() { return processed.get(); }
    public long duplicateCount() { return duplicates.get(); }
    public long failureCount() { return failures.get(); }
    public long deadLetterCount() { return deadLetters.get(); }
}
