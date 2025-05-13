package com.floo.restaurant_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String orderId; // From order-service
    private String restaurantId;
    private String userId;
    private List<OrderItem> items;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Address deliveryAddress;

    public enum OrderStatus {
        RECEIVED, PREPARING, FINISHED, PACKING, READY_FOR_PICKUP, PICKED_UP, DELIVERED
    }
}

