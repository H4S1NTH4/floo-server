package com.floo.order_service.feign;

import com.floo.order_service.dto.OrderNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("RESTAURANT-SERVICE")
public interface RestaurantInterface {
    // Define methods to interact with the restaurant service
    // For example:
    // @GetMapping("/api/v1/restaurant/{id}")
    // ResponseEntity<Restaurant> getRestaurantById(@PathVariable String id);

    @PostMapping("/api/v1/restaurant/order/notify")
    ResponseEntity<String> notifyRestaurant(@RequestBody String restaurantId, OrderNotificationDto orderNotification);



}
