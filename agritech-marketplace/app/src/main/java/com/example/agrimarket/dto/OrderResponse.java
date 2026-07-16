package com.example.agrimarket.dto;

import com.example.agrimarket.entity.Order;
import com.example.agrimarket.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        Long buyerId,
        String buyerName,
        Long produceId,
        String produceName,
        BigDecimal quantity,
        BigDecimal totalPrice,
        OrderStatus status,
        Instant orderDate,
        Instant createdAt,
        Instant updatedAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getBuyer().getId(),
                order.getBuyer().getName(),
                order.getProduce().getId(),
                order.getProduce().getName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getOrderDate(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
