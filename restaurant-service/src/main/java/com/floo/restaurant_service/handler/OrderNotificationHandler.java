package com.floo.restaurant_service.handler;

import com.floo.restaurant_service.feign.PaymentServiceClient;
import com.floo.restaurant_service.model.Order;
import com.floo.restaurant_service.model.OrderItem;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.service.RestaurantService;
import com.floo.restaurant_service.utils.OrderPlacedNotification;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderNotificationHandler {
    private final RestaurantService restaurantService;
    private final PaymentServiceClient paymentServiceClient;
    private static final Logger logger = LoggerFactory.getLogger(OrderNotificationHandler.class);

    public void handleOrderNotification(OrderPlacedNotification notification) {
        if (restaurantService.getRestaurant(notification.getRestaurantId())
                .getRestaurantDto()
                .getActiveOrders()
                .stream()
                .anyMatch(o -> o.getOrderId().equals(notification.getOrderId()))) {
            logger.warn("Order {} already exists", notification.getOrderId());
            return;
        }

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
                .paymentStatus(Order.PaymentStatus.PENDING)
                .build();

        // Calculate total price would require fetching menu items
        // For now, we'll set it to 0 and update later
        // Get payment status
        try {
            Order.PaymentStatus status = paymentServiceClient.getPaymentStatus(order.getOrderId());
            order.setPaymentStatus(status);
        } catch (Exception e) {
            logger.error("Error fetching payment status for order {}", order.getOrderId(), e);
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
        }

        restaurantService.addOrderToRestaurant(notification.getRestaurantId(), order);
    }
}