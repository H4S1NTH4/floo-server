package com.floo.order_service.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("RESTAURANT-SERVICE")
public interface RestaurantInterface {
    // Define methods to interact with the restaurant service
    // For example:
    // @GetMapping("/api/v1/restaurant/{id}")
    // ResponseEntity<Restaurant> getRestaurantById(@PathVariable String id);

}
