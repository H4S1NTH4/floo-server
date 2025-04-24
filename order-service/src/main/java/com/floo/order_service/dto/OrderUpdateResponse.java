package com.floo.order_service.dto;

import com.floo.order_service.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor

@NoArgsConstructor
public class OrderUpdateResponse {
    private String message;
    private Order order;
}
