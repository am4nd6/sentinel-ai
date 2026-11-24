package com.sentinelai.pipeline.application;

import java.time.Duration; import java.time.Instant; import java.util.*; import java.util.concurrent.ConcurrentHashMap; import org.springframework.stereotype.Service;

@Service
public class CorrelationService {
    private final Map<String, List<Signal>> windows = new ConcurrentHashMap<>();
    public synchronized Set<String> correlate(String tenant, String account, String device, String source, Instant occurredAt, Duration window) {
        String key = tenant + ":" + account;
        List<Signal> signals = windows.computeIfAbsent(key, ignored -> new ArrayList<>());
        signals.removeIf(signal -> signal.at().isBefore(occurredAt.minus(window)));
        signals.add(new Signal(device, source, occurredAt));
        Set<String> keys = new HashSet<>();
        signals.forEach(signal -> { keys.add("account:" + account); keys.add("device:" + signal.device()); keys.add("source:" + signal.source()); });
        return keys;
    }
    private record Signal(String device, String source, Instant at) {}
}
