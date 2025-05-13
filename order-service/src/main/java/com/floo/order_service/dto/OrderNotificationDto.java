package com.floo.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNotificationDto {
    private String orderId;
    private String status;
    private String customerId;
    private String restaurantId;
    private String deliveryAddress;
    private Long timestamp;
}
