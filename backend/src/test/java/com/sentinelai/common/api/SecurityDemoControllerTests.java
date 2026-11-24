package com.sentinelai.common.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityDemoControllerTests {
    @Autowired MockMvc mockMvc;

    @Test void unauthenticatedRequestIsRejected() throws Exception {
        mockMvc.perform(get("/api/v1/me")).andExpect(status().isUnauthorized());
    }

    @Test void adminEndpointRejectsAnalyst() throws Exception {
        mockMvc.perform(get("/api/v1/admin/policies").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ANALYST"))))
                .andExpect(status().isForbidden());
    }

    @Test void tenantScopeRejectsCrossTenant() throws Exception {
        mockMvc.perform(get("/api/v1/tenants/tenant_b/resource").with(jwt().jwt(jwt -> jwt.claim("tenant_id", "tenant_a"))))
                .andExpect(status().isForbidden());
    }
}
