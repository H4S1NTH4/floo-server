package com.floo.restaurant_service.handler;

import com.floo.restaurant_service.dto.PaymentConfirmation;
import com.floo.restaurant_service.feign.NotificationClient;
import com.floo.restaurant_service.model.Order;
import com.floo.restaurant_service.service.RestaurantService;
import com.floo.restaurant_service.utils.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConfirmationHandler {
    private final RestaurantService restaurantService;
    private final NotificationClient notificationClient;
    private static final Logger logger = LoggerFactory.getLogger(PaymentConfirmationHandler.class);

    public void handlePaymentConfirmation(PaymentConfirmation confirmation) {
        try {
            restaurantService.updateOrderPaymentStatus(
                    confirmation.getRestaurantId(),
                    confirmation.getOrderId(),
                    Order.PaymentStatus.COMPLETED
            );

            // Notify restaurant
            notificationClient.sendNotification(
                    NotificationRequest.builder()
                            .receiverUsername(restaurantService.getRestaurantOwner(confirmation.getRestaurantId()))
                            .title("Payment Confirmed")
                            .message(String.format("Payment confirmed for order #%s", confirmation.getOrderId()))
                            .build()
            );
        } catch (Exception e) {
            logger.error("Error processing payment confirmation for order {}", confirmation.getOrderId(), e);
        }
    }
}
