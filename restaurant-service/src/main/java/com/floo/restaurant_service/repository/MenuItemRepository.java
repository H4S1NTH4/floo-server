package com.floo.restaurant_service.repository;

import com.floo.restaurant_service.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    // Find all menu items by restaurant ID
    List<MenuItem> findByRestaurantId(String restaurantId);

    // Find menu items by restaurant ID and category
    List<MenuItem> findByRestaurantIdAndCategory(String restaurantId, String category);

    // Find top selling menu items by restaurant ID (ordered by totalSales descending)
    List<MenuItem> findByRestaurantIdOrderByTotalSalesDesc(String restaurantId);

    // Find menu item by ID and restaurant ID (for extra validation)
    Optional<MenuItem> findByIdAndRestaurantId(String id, String restaurantId);
}
