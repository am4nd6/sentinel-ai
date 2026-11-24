package com.sentinelai.ingestion.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class EventIngestionControllerTests {
    @Autowired MockMvc mockMvc;
    private static final String BODY = "{\"eventId\":\"evt_demo_900\",\"eventType\":\"auth.login.failed\",\"schemaVersion\":\"1.0\",\"occurredAt\":\"2026-11-24T12:00:00Z\",\"attributes\":{\"attempts\":3}}";

    @Test void rejectsUnauthenticatedIngestion() throws Exception { mockMvc.perform(post("/api/v1/events").contentType("application/json").content(BODY)).andExpect(status().isUnauthorized()); }
    @Test void acceptsAuthenticatedEventWithTenant() throws Exception { mockMvc.perform(post("/api/v1/events").with(jwt().jwt(jwt -> jwt.claim("tenant_id", "tenant_demo_a"))).header("Idempotency-Key", "idem-900").contentType("application/json").content(BODY)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ACCEPTED")); }
    @Test void rejectsInvalidPayload() throws Exception { mockMvc.perform(post("/api/v1/events").with(jwt().jwt(jwt -> jwt.claim("tenant_id", "tenant_demo_a"))).contentType("application/json").content("{}" )).andExpect(status().isBadRequest()); }
}
