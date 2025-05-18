package com.floo.restaurant_service.dto;

import com.floo.restaurant_service.model.Address;
import com.floo.restaurant_service.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String id;
    private String orderId; // From order-service
    private String restaurantId;
    private String userId;
    private List<OrderItemDto> items;
    private BigDecimal totalPrice;
    private Order.OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Address deliveryAddress;
}

