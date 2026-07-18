package com.example.agrimarket.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
        @NotNull(message = "buyerId is required")
        Long buyerId,

        @NotNull(message = "produceId is required")
        Long produceId,

        @NotNull(message = "quantity is required")
        @Positive(message = "quantity must be greater than zero")
        @Digits(integer = 8, fraction = 2, message = "quantity format is invalid")
        java.math.BigDecimal quantity
) {
}
