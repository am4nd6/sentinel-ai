package com.sentinelai.ingestion.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.sentinelai.ingestion.domain.SecurityEvent;
import com.sentinelai.ingestion.infrastructure.SecurityEventRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/events")
public class EventIngestionController {
    private final SecurityEventRepository repository;
    public EventIngestionController(SecurityEventRepository repository) { this.repository = repository; }

    @PostMapping
    public Map<String, Object> ingest(@Valid @RequestBody EventRequest request,
                                      @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
                                      Authentication authentication) {
        String tenant = tenantOf(authentication);
        String dedupe = idempotencyKey == null || idempotencyKey.isBlank() ? request.eventId : idempotencyKey;
        var existing = repository.findByTenantIdAndDedupeKey(tenant, dedupe);
        if (existing.isPresent()) return Map.of("eventId", existing.get().getId(), "status", "DUPLICATE");
        SecurityEvent event = new SecurityEvent(UUID.randomUUID(), tenant, request.eventId, request.eventType,
                request.schemaVersion, request.occurredAt, request.accountRef, request.deviceRef, request.sourceRef,
                request.attributes.toString(), dedupe);
        repository.save(event);
        return Map.of("eventId", event.getId(), "status", "ACCEPTED");
    }

    private String tenantOf(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken token)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tenant missing");
        String tenant = token.getToken().getClaimAsString("tenant_id");
        if (tenant == null || tenant.isBlank()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tenant missing");
        return tenant;
    }

    public static class EventRequest {
        @NotBlank public String eventId;
        @NotBlank public String eventType;
        @NotBlank public String schemaVersion;
        @NotNull public Instant occurredAt;
        public String accountRef;
        public String deviceRef;
        public String sourceRef;
        @NotNull public JsonNode attributes;
    }
}
