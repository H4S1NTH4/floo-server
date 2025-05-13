package com.floo.restaurant_service.controller;


import com.floo.restaurant_service.dto.MenuItemDto;
import com.floo.restaurant_service.dto.RestaurantDto;
import com.floo.restaurant_service.dto.RestaurantRequest;
import com.floo.restaurant_service.dto.RestaurantResponse;
import com.floo.restaurant_service.model.Order;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addRestaurant(@RequestBody RestaurantRequest request) {
        return restaurantService.addRestaurant(request);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantResponse getRestaurant(@PathVariable("id") String id) {
        return restaurantService.getRestaurant(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateRestaurant(@PathVariable String id, @RequestBody RestaurantRequest request) {
        return restaurantService.updateRestaurant(id, request);
    }

    @PutMapping("/{id}/availability")
    @ResponseStatus(HttpStatus.OK)
    public String updateAvailability(@PathVariable String id,
                                     @RequestParam boolean isAvailable) {
        return restaurantService.updateAvailability(id, isAvailable);
    }

    @GetMapping("/pending")
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantDto> getPendingRestaurants() {
        return restaurantService.getPendingRestaurants();
    }

    @PutMapping("/{id}/verify")
    @ResponseStatus(HttpStatus.OK)
    public String verifyRestaurant(@PathVariable String id,
                                   @RequestParam boolean isApproved) {
        return restaurantService.verifyRestaurant(id, isApproved);
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public String updateRestaurantStatus(@PathVariable String id,
                                         @RequestParam Restaurant.RestaurantStatus status) {
        return restaurantService.updateRestaurantStatus(id, status);
    }

    @PostMapping("/{id}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public String addOrder(@PathVariable String id, @RequestBody Order order) {
        return restaurantService.addOrderToRestaurant(id, order);
    }

    @PutMapping("/{restaurantId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public String updateOrderStatus(@PathVariable String restaurantId,
                                    @PathVariable String orderId,
                                    @RequestParam Order.OrderStatus status) {
        return restaurantService.updateOrderStatus(restaurantId, orderId, status);
    }

    @GetMapping("/{id}/orders/active")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getActiveOrders(@PathVariable String id) {
        return restaurantService.getActiveOrders(id);
    }

    @GetMapping("/{id}/orders/past")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getPastOrders(@PathVariable String id) {
        return restaurantService.getPastOrders(id);
    }

    @GetMapping("/test")
    public String testRestaurantService() {
        return "Restaurant Service is running!";
    }
}
