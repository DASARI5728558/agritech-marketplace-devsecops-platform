package com.example.agrimarket.dto;

import com.example.agrimarket.entity.Buyer;
import java.time.Instant;

public record BuyerResponse(
        Long id,
        String name,
        String email,
        String phone,
        String deliveryAddress,
        Instant createdAt,
        Instant updatedAt
) {
    public static BuyerResponse from(Buyer buyer) {
        return new BuyerResponse(
                buyer.getId(),
                buyer.getName(),
                buyer.getEmail(),
                buyer.getPhone(),
                buyer.getDeliveryAddress(),
                buyer.getCreatedAt(),
                buyer.getUpdatedAt()
        );
    }
}
