package com.floo.restaurant_service.dto;

import com.floo.restaurant_service.model.Address;
import com.floo.restaurant_service.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDto {
    private String id;
    private String name;
    private String description;
    private Address address;
    private List<Long> contactInfo;
    private Double rating;
    private List<MenuItem> menuItems;
    private boolean isAvailable;  // New field
    private boolean isVerified;
}