package com.floo.restaurant_service.controller;

import com.floo.restaurant_service.dto.OrderDto;
import com.floo.restaurant_service.dto.RestaurantDto;
import com.floo.restaurant_service.model.Order;
import com.floo.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final RestaurantService restaurantService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    // View all incoming orders
    @GetMapping("/restaurant/{restaurantId}/active")
    public ResponseEntity<List<OrderDto>> getActiveOrders(
            @PathVariable String restaurantId) {
        logger.info("Fetching active orders for restaurant ID: {}", restaurantId);
        try {
            List<OrderDto> orders = restaurantService.getActiveOrders(restaurantId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching active orders: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // View an order
    @GetMapping("/{orderId}/restaurant/{restaurantId}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable String restaurantId,
            @PathVariable String orderId) {
        logger.info("Fetching order ID: {} for restaurant ID: {}", orderId, restaurantId);
        try {
            OrderDto order = restaurantService.getOrder(restaurantId, orderId);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            logger.error("Error fetching order: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // View completed orders (daily and all)
    @GetMapping("/restaurant/{restaurantId}/completed")
    public ResponseEntity<List<OrderDto>> getCompletedOrders(
            @PathVariable String restaurantId,
            @RequestParam(required = false) LocalDate date) {
        logger.info("Fetching completed orders for restaurant ID: {}", restaurantId);
        try {
            List<OrderDto> orders = restaurantService.getCompletedOrders(restaurantId, date);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching completed orders: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Update order status
    @PutMapping("/{orderId}/restaurant/{restaurantId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable String restaurantId,
            @PathVariable String orderId,
            @RequestParam Order.OrderStatus status) {
        logger.info("Updating order ID: {} to status: {}", orderId, status);
        try {
            String result = restaurantService.updateOrderStatus(restaurantId, orderId, status);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error updating order status: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error updating order status");
        }
    }

    // View current orders being prepared
    @GetMapping("/restaurant/{restaurantId}/in-progress")
    public ResponseEntity<List<OrderDto>> getInProgressOrders(
            @PathVariable String restaurantId) {
        logger.info("Fetching in-progress orders for restaurant ID: {}", restaurantId);
        try {
            List<OrderDto> orders = restaurantService.getInProgressOrders(restaurantId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching in-progress orders: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}