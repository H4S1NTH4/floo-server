package com.floo.restaurant_service.repository;

import com.floo.restaurant_service.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    // Find orders by restaurant ID
    List<Order> findByRestaurantId(String restaurantId);

    // Find orders by restaurant ID and status
    List<Order> findByRestaurantIdAndStatus(String restaurantId, Order.OrderStatus status);

    // Find completed orders within a date range
    List<Order> findByRestaurantIdAndUpdatedAtBetween(
            String restaurantId,
            LocalDateTime start,
            LocalDateTime end);
}