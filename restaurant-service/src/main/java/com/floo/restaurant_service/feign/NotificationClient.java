package com.floo.restaurant_service.feign;

import com.floo.restaurant_service.utils.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "notification-service", path = "/api/v1/notifications")
public interface NotificationClient {

    @PostMapping
    void sendNotification(NotificationRequest request);
}