package com.floo.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusChange {
    private OrderStatus status;
    private Long timestamp;
}