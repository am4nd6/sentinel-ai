package com.sentinelai.common.api;

import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SecurityDemoController {
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken token)) return Map.of("subject", authentication.getName());
        return Map.of("subject", token.getToken().getSubject(), "tenantId", token.getToken().getClaimAsString("tenant_id"), "roles", token.getAuthorities());
    }

    @GetMapping("/admin/policies")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminPolicies() { return Map.of("status", "authorized", "scope", "admin"); }

    @GetMapping("/tenants/{tenantId}/resource")
    public Map<String, String> tenantResource(@PathVariable String tenantId, Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken token) || !tenantId.equals(token.getToken().getClaimAsString("tenant_id"))) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Tenant scope denied");
        }
        return Map.of("tenantId", tenantId, "status", "authorized");
    }
}
