package com.floo.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponse {
    private RestaurantDto restaurantDto;
    private Integer responseCode;
    private String msg;
}