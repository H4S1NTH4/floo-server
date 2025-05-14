package com.floo.order_service.dto;

import com.floo.order_service.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddResponse {
    private String message;
    private Order order;
}
