package com.floo.order_service.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private Long orderNumber;
    private String customerId;
    private String restaurantId;
    private String restaurantName;
    private List<OrderItem> orderItems;
    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;
    private Long orderTime;
    private String deliveryAddress;
    private String restaurantAddress;
    private String driverId;
    private Long expectedDeliveryTime;
    private Long deliveryTime;
    private OrderStatus orderStatus;
    private String paymentId;
    private String userId;
    private List<StatusChange> statusHistory;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;


}