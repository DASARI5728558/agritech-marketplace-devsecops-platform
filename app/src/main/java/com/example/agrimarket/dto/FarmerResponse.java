package com.example.agrimarket.dto;

import com.example.agrimarket.entity.Farmer;
import java.time.Instant;

public record FarmerResponse(
        Long id,
        String name,
        String email,
        String phone,
        String farmName,
        String location,
        Instant createdAt,
        Instant updatedAt
) {
    public static FarmerResponse from(Farmer farmer) {
        return new FarmerResponse(
                farmer.getId(),
                farmer.getName(),
                farmer.getEmail(),
                farmer.getPhone(),
                farmer.getFarmName(),
                farmer.getLocation(),
                farmer.getCreatedAt(),
                farmer.getUpdatedAt()
        );
    }
}
