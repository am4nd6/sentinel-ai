package com.sentinelai.common.api;

import com.sentinelai.common.security.AdminAuditService;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/audit")
public class AdminAuditController {
    private final AdminAuditService auditService;

    public AdminAuditController(AdminAuditService auditService) { this.auditService = auditService; }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> record(@RequestBody AuditRequest request, Authentication actor) {
        auditService.record(actor, request.action(), request.resource(), request.outcome());
        return Map.of("status", "recorded");
    }

    public record AuditRequest(String action, String resource, String outcome) { }
}
