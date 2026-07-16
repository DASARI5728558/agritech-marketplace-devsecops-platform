package com.example.agrimarket.dto;

import java.time.Instant;

public record HealthResponse(
        String status,
        String service,
        String environment,
        Instant timestamp
) {
}
