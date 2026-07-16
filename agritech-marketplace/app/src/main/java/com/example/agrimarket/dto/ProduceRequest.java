package com.example.agrimarket.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProduceRequest(
        @NotBlank(message = "name is required")
        @Size(max = 140, message = "name must be less than 140 characters")
        String name,

        @Size(max = 60, message = "category must be less than 60 characters")
        String category,

        @NotBlank(message = "unit is required")
        @Size(max = 20, message = "unit must be less than 20 characters")
        String unit,

        @NotNull(message = "pricePerUnit is required")
        @PositiveOrZero(message = "pricePerUnit must be zero or positive")
        @Digits(integer = 8, fraction = 2, message = "pricePerUnit format is invalid")
        BigDecimal pricePerUnit,

        @NotNull(message = "quantityAvailable is required")
        @PositiveOrZero(message = "quantityAvailable must be zero or positive")
        @Digits(integer = 8, fraction = 2, message = "quantityAvailable format is invalid")
        BigDecimal quantityAvailable,

        Boolean organic,

        @PastOrPresent(message = "harvestDate cannot be in the future")
        LocalDate harvestDate,

        @Size(max = 1000, message = "description must be less than 1000 characters")
        String description,

        @NotNull(message = "farmerId is required")
        Long farmerId
) {
}
