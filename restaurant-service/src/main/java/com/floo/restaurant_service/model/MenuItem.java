package com.floo.restaurant_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "menu_items")
public class MenuItem {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Binary icon;
    private Integer quantity;
    private String category;
    private String restaurantId;
    private int totalSales = 0;
}
