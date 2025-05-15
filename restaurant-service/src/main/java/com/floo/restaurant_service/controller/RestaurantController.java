package com.floo.restaurant_service.controller;

import com.floo.restaurant_service.dto.*;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/restaurant")
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {
    private final RestaurantService restaurantService;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(RestaurantController.class);

    // Create my restaurant (called from auth-service during registration)
    @PostMapping
    public ResponseEntity<String> createMyRestaurant(@RequestBody RestaurantRequest request) {
        logger.info("Creating new restaurant with name: {}", request.getName());
        try {
            String result = restaurantService.addRestaurant(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating restaurant: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error creating restaurant");
        }
    }

    // Admin retrives all verified restaurants
    @GetMapping("/admin/all")
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        logger.info("Admin requesting all restaurants");
        try {
            List<RestaurantDto> restaurants = restaurantService.getAllRestaurants();
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            logger.error("Error fetching all restaurants for admin: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // View my restaurant details
    @GetMapping("/my/{id}")
    public ResponseEntity<RestaurantResponse> getMyRestaurant(@PathVariable String id) {
        logger.info("Fetching restaurant details for ID: {}", id);
        RestaurantResponse response = restaurantService.getRestaurant(id);
        if (response.getResponseCode() == 404) {
            logger.warn("Restaurant not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    // Update my restaurant
    @PutMapping("/my/{id}")
    public ResponseEntity<String> updateMyRestaurant(@PathVariable String id, @RequestBody RestaurantRequest request) {
        logger.info("Updating restaurant with ID: {}", id);
        String result = restaurantService.updateRestaurant(id, request);
        if (result.equals("Restaurant not found")) {
            logger.warn("Restaurant not found for update with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    // Delete my restaurant
    @DeleteMapping("/my/{id}")
    public ResponseEntity<String> deleteMyRestaurant(@PathVariable String id) {
        logger.info("Deleting restaurant with ID: {}", id);
        try {
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.ok("Restaurant deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting restaurant: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error deleting restaurant");
        }
    }

    // Admin - Get pending restaurants for verification
    @GetMapping("/admin/pending")
    public ResponseEntity<List<RestaurantDto>> getPendingRestaurants() {
        logger.info("Fetching all pending restaurants for admin");
        return ResponseEntity.ok(restaurantService.getPendingRestaurants());
    }

    // Admin - Verify restaurant
    @PutMapping("/admin/verify/{id}")
    public ResponseEntity<String> verifyRestaurant(@PathVariable String id, @RequestParam boolean isApproved) {
        logger.info("Admin verifying restaurant ID: {}, approved: {}", id, isApproved);
        String result = restaurantService.verifyRestaurant(id, isApproved);
        if (result.equals("Restaurant not found")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    // update the availability status of the restaurant
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateRestaurantStatus(
            @PathVariable String id,
            @RequestParam Restaurant.RestaurantStatus status) {
        logger.info("Updating status for restaurant ID: {} to {}", id, status);
        try {
            String result = restaurantService.updateRestaurantStatus(id, status);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("Error updating restaurant status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Daily income
    @GetMapping("/my/{id}/income/daily")
    public ResponseEntity<DailyIncomeResponse> getDailyIncome(
            @PathVariable String id,
            @RequestParam(required = false) LocalDate date) {
        logger.info("Fetching daily income for restaurant ID: {}", id);
        if (date == null) {
            date = LocalDate.now();
        }
        DailyIncomeResponse response = restaurantService.getDailyIncome(id, date);
        return ResponseEntity.ok(response);
    }

    // Total income
    @GetMapping("/my/{id}/income/total")
    public ResponseEntity<TotalIncomeResponse> getTotalIncome(@PathVariable String id) {
        logger.info("Fetching total income for restaurant ID: {}", id);
        TotalIncomeResponse response = restaurantService.getTotalIncome(id);
        return ResponseEntity.ok(response);
    }


    // Submit feedback
    @PostMapping("/my/{id}/feedback")
    public ResponseEntity<String> submitFeedback(
            @PathVariable String id,
            @RequestBody FeedbackRequest feedback) {
        logger.info("Submitting feedback for restaurant ID: {}", id);
        try {
            restaurantService.addFeedback(id, feedback);
            return ResponseEntity.ok("Feedback submitted successfully");
        } catch (Exception e) {
            logger.error("Error submitting feedback: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error submitting feedback");
        }
    }

    @GetMapping("/test")
    public String testRestaurantService() {
        return "Restaurant Service is running!";
    }
}
