package com.example.agrimarket.controller;

import com.example.agrimarket.dto.HealthResponse;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${spring.profiles.active:local}")
    private String environment;

    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse("UP", serviceName, environment, Instant.now());
    }
}
