package com.floo.restaurant_service.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class OrderPlacedNotification {
    private String orderId;
    private List<String> menuItemIds;
    private List<Integer> menuItemQuantities;
    private Address orderAddress;
    private String userId;

    @JsonCreator
    public OrderPlacedNotification(@JsonProperty("orderId") String orderId,
                                   @JsonProperty("menuItemIds") List<String> menuItemIds,
                                   @JsonProperty("menuItemQuantities") List<Integer> menuItemQuantities,
                                   @JsonProperty("orderAddress") Address orderAddress,
                                   @JsonProperty("userId") String userId) {
        this.orderId = orderId;
        this.menuItemIds = menuItemIds;
        this.menuItemQuantities = menuItemQuantities;
        this.orderAddress = orderAddress;
        this.userId = userId;
    }

}
