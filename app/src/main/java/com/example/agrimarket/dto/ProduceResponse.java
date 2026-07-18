package com.example.agrimarket.dto;

import com.example.agrimarket.entity.Produce;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record ProduceResponse(
        Long id,
        String name,
        String category,
        String unit,
        BigDecimal pricePerUnit,
        BigDecimal quantityAvailable,
        boolean organic,
        LocalDate harvestDate,
        String description,
        Long farmerId,
        String farmerName,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProduceResponse from(Produce produce) {
        return new ProduceResponse(
                produce.getId(),
                produce.getName(),
                produce.getCategory(),
                produce.getUnit(),
                produce.getPricePerUnit(),
                produce.getQuantityAvailable(),
                produce.isOrganic(),
                produce.getHarvestDate(),
                produce.getDescription(),
                produce.getFarmer().getId(),
                produce.getFarmer().getName(),
                produce.getCreatedAt(),
                produce.getUpdatedAt()
        );
    }
}
