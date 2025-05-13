package com.floo.restaurant_service.feign;

import com.floo.restaurant_service.utils.OrderPlacedNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    @PostMapping("/api/v1/orders/notify-restaurant")
    void notifyRestaurant(@RequestBody OrderPlacedNotification notification);
}