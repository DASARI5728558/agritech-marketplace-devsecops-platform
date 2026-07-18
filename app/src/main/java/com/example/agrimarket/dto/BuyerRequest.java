package com.example.agrimarket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BuyerRequest(
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be less than 120 characters")
        String name,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        @Size(max = 160, message = "email must be less than 160 characters")
        String email,

        @Size(max = 20, message = "phone must be less than 20 characters")
        String phone,

        @Size(max = 250, message = "deliveryAddress must be less than 250 characters")
        String deliveryAddress
) {
}
