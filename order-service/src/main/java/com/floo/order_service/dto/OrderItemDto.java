package com.floo.order_service.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class OrderItemDto {
    private String id;
    private String name;
    private Integer price;
    private Integer quantity;
    private String imageUrl;
}