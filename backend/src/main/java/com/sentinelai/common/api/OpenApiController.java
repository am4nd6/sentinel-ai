package com.sentinelai.common.api;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {
    @GetMapping("/v3/api-docs")
    public Map<String, Object> apiDocs() {
        return Map.of("openapi", "3.0.3", "info", Map.of("title", "SENTINEL AI API", "version", "0.1.0"),
                "paths", Map.of("/api/v1/health", Map.of("get", Map.of("summary", "Health check"))));
    }
}
