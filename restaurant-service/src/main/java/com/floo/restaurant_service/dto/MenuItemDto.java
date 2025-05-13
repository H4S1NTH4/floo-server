package com.floo.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemDto {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Binary icon;
    private Integer quantity;
    private String restaurantId;
}
