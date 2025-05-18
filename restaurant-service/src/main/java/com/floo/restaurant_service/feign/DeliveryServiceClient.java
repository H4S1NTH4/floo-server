package com.floo.restaurant_service.feign;

import com.floo.restaurant_service.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service")
public interface DeliveryServiceClient {
    @PostMapping("/api/v1/deliveries/notify-ready")
    void notifyDeliveryReady(@RequestBody Order order);
}