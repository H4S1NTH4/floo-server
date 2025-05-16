package com.floo.restaurant_service.handler;

import com.floo.restaurant_service.feign.NotificationClient;
import com.floo.restaurant_service.feign.PaymentServiceClient;
import com.floo.restaurant_service.model.MenuItem;
import com.floo.restaurant_service.model.Order;
import com.floo.restaurant_service.model.OrderItem;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.service.RestaurantService;
import com.floo.restaurant_service.service.MenuItemService;
import com.floo.restaurant_service.utils.NotificationRequest;
import com.floo.restaurant_service.utils.OrderPlacedNotification;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderNotificationHandler {
    private final RestaurantService restaurantService;
    private final PaymentServiceClient paymentServiceClient;
    private final MenuItemService menuItemService;
    private final NotificationClient notificationClient;
    private static final Logger logger = LoggerFactory.getLogger(OrderNotificationHandler.class);

    public void handleOrderNotification(OrderPlacedNotification notification) {
        // Check if order already exists
        if (restaurantService.orderExists(notification.getRestaurantId(), notification.getOrderId())) {
            logger.warn("Order {} already exists", notification.getOrderId());
            return;
        }

        // Fetch menu items to calculate total price
        List<MenuItem> menuItems = menuItemService.getMenuItemsByIds(notification.getMenuItemIds());
        BigDecimal totalPrice = calculateTotalPrice(menuItems, notification.getMenuItemQuantities());

        // Build order
        Order order = Order.builder()
                .orderId(notification.getOrderId())
                .userId(notification.getUserId())
                .items(mapToOrderItems(notification, menuItems))
                .totalPrice(totalPrice.doubleValue())
                .deliveryAddress(notification.getOrderAddress())
                .status(Order.OrderStatus.RECEIVED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Verify payment status
        try {
            Order.PaymentStatus status = paymentServiceClient.getPaymentStatus(order.getOrderId());
            order.setPaymentStatus(status);
        } catch (Exception e) {
            logger.error("Error fetching payment status for order {}", order.getOrderId(), e);
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
        }

        // Add order to restaurant
        restaurantService.addOrderToRestaurant(notification.getRestaurantId(), order);

        // Notify restaurant
        notificationClient.sendNotification(
                NotificationRequest.builder()
                        .receiverUsername(restaurantService.getRestaurantOwner(notification.getRestaurantId()))
                        .title("New Order Received")
                        .message(String.format("New order #%s received", notification.getOrderId()))
                        .build()
        );
    }

    private BigDecimal calculateTotalPrice(List<MenuItem> menuItems, List<Integer> quantities) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < menuItems.size(); i++) {
            total = total.add(menuItems.get(i).getPrice().multiply(BigDecimal.valueOf(quantities.get(i))));
        }
        return total;
    }

    private List<OrderItem> mapToOrderItems(OrderPlacedNotification notification, List<MenuItem> menuItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            orderItems.add(OrderItem.builder()
                    .menuItemId(item.getId())
                    .name(item.getName())
                    .quantity(notification.getMenuItemQuantities().get(i))
                    .price(item.getPrice())
                    .build());
        }
        return orderItems;
    }
}