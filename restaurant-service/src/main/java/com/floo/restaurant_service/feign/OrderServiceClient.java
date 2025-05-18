package com.floo.restaurant_service.feign;

import com.floo.restaurant_service.dto.FeedbackRequest;
import com.floo.restaurant_service.utils.OrderPlacedNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    @PostMapping("/api/v1/orders/notify-restaurant")
    void notifyRestaurant(@RequestBody OrderPlacedNotification notification);

    @PostMapping("/api/v1/orders/feedback")
    void submitFeedback(@RequestBody FeedbackRequest request);
}


