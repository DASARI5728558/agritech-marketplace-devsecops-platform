package com.example.agrimarket.controller;

import com.example.agrimarket.dto.HealthResponse;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root-level /health endpoint kept separate from /api/v1/health so that
 * container HEALTHCHECK directives (see Dockerfile) can probe the app
 * without depending on the versioned API path.
 */
@RestController
public class RootHealthController {

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${spring.profiles.active:local}")
    private String environment;

    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse("UP", serviceName, environment, Instant.now());
    }
}
