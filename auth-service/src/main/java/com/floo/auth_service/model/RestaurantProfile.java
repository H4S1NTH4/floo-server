package com.floo.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantProfile {
    private String restaurantName;
    private String address;
    private String contactNumber;
    private String restaurantImageUrl; // Logo or image of restaurant
}
