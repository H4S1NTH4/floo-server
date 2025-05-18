package com.floo.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmation {
    private String orderId;
    private String restaurantId;
    private BigDecimal amount;
    private LocalDateTime paymentTime;
}