package com.floo.restaurant_service.repository;
import com.floo.restaurant_service.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    Optional<Restaurant> findByOwnerUsername(String username);
    List<Restaurant> findByIsVerifiedFalse();
}