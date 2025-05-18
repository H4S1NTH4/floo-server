package com.floo.restaurant_service.repository;
import com.floo.restaurant_service.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    Optional<Restaurant> findByOwnerUsername(String username);
    // Get all restaurants (used by admin)
    List<Restaurant> findAll();

    // Get pending restaurants (isVerified = false)
    List<Restaurant> findByIsVerifiedFalse();

    // Find restaurants by status
    List<Restaurant> findByStatus(Restaurant.RestaurantStatus status);

    Optional<Restaurant> findById(String id);

    // Find restaurant by owner ID (if you need this later)
    // List<Restaurant> findByOwner_OwnerId(String ownerId);
}