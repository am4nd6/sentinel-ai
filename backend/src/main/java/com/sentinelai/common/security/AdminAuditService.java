package com.sentinelai.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AdminAuditService {
    private static final Logger log = LoggerFactory.getLogger(AdminAuditService.class);

    public void record(Authentication actor, String action, String resource, String outcome) {
        // Never log tokens, passwords, prompts or complete request payloads.
        log.info("admin_audit actor={} action={} resource={} outcome={}", actor.getName(), action, resource, outcome);
    }
}
