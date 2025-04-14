package com.floo.restaurant_service.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    @GetMapping
    public String getAllRestaurants() {
        return "List of restaurants will be here";
    }

    @GetMapping("/{id}")
    public String getRestaurant(@PathVariable String id) {
        return "Details for restaurant " + id;
    }

    @PostMapping
    public String createRestaurant() {
        return "Restaurant created";
    }
}
