package com.floo.order_service.repository;

import com.floo.order_service.model.Order;
import com.floo.order_service.model.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    // Custom query methods can be defined here if needed
    // For example, find by userId or status
    List<Order> findByCustomerId(String customerId);
    List<Order> findByRestaurantId(String restaurantId);
    Order findByIdAndOrderStatus(String id, OrderStatus status);

}
