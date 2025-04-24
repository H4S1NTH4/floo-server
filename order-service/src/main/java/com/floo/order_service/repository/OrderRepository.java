package com.floo.order_service.repository;

import com.floo.order_service.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    // Custom query methods can be defined here if needed
    // For example, find by userId or status
}
