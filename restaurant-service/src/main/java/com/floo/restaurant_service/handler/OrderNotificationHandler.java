package com.floo.restaurant_service.handler;

import com.floo.restaurant_service.model.Order;
import com.floo.restaurant_service.model.OrderItem;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.service.RestaurantService;
import com.floo.restaurant_service.utils.OrderPlacedNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderNotificationHandler {
    private final RestaurantService restaurantService;

    public void handleOrderNotification(OrderPlacedNotification notification) {
        Order order = Order.builder()
                .orderId(notification.getOrderId())
                .userId(notification.getUserId())
                .items(notification.getMenuItemIds().stream()
                        .map(id -> OrderItem.builder()
                                .menuItemId(id)
                                .quantity(notification.getMenuItemQuantities().get(
                                        notification.getMenuItemIds().indexOf(id)))
                                .build())
                        .collect(Collectors.toList()))
                .deliveryAddress(notification.getOrderAddress())
                .build();

        // Calculate total price would require fetching menu items
        // For now, we'll set it to 0 and update later
        order.setTotalPrice(0.0);

        restaurantService.addOrderToRestaurant(notification.getRestaurantId(), order);
    }
}