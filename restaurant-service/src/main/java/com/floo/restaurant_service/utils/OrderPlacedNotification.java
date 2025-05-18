package com.floo.restaurant_service.utils;

import com.floo.restaurant_service.model.Address;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedNotification {
    private String orderId;
    private List<String> menuItemIds;
    private List<Integer> menuItemQuantities;
    private Address orderAddress;
    private String userId;
    private String restaurantId;
}