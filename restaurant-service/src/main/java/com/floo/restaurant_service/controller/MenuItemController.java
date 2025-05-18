package com.floo.restaurant_service.controller;

import com.floo.restaurant_service.dto.*;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/menuitem")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;
    private static final Logger logger = LoggerFactory.getLogger(MenuItemController.class);

    // Create a menu item
    @PostMapping
    public ResponseEntity<String> createMenuItem(@RequestBody MenuItemDto menuItemDto) {
        logger.info("Creating new menu item: {}", menuItemDto.getName());
        try {
            String result = menuItemService.addMenuItem(menuItemDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating menu item: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error creating menu item");
        }
    }

    // Delete a menu item
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable String itemId) {
        logger.info("Deleting menu item with ID: {}", itemId);
        String result = menuItemService.deleteMenuItem(itemId);
        if (result.equals("Menu item not found")) {
            logger.warn("Menu item not found for deletion: {}", itemId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    // Update a menu item
    @PutMapping("/{itemId}")
    public ResponseEntity<String> updateMenuItem(
            @PathVariable String itemId,
            @RequestBody MenuItemDto menuItemDto) {
        logger.info("Updating menu item with ID: {}", itemId);
        String result = menuItemService.updateMenuItem(itemId, menuItemDto);
        if (result.equals("Menu item not found")) {
            logger.warn("Menu item not found for update: {}", itemId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    // View a menu item
    @GetMapping("/{itemId}")
    public ResponseEntity<MenuItemDto> getMenuItem(@PathVariable String itemId) {
        logger.info("Fetching menu item with ID: {}", itemId);
        MenuItemDto menuItem = menuItemService.getMenuItemById(itemId);
        if (menuItem == null) {
            logger.warn("Menu item not found: {}", itemId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menuItem);
    }

    // View all menu items in the restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDto>> getAllMenuItemsByRestaurant(
            @PathVariable String restaurantId) {
        logger.info("Fetching all menu items for restaurant ID: {}", restaurantId);
        return ResponseEntity.ok(menuItemService.getAllMenuItems(restaurantId));
    }

    // View menu items by category
    @GetMapping("/restaurant/{restaurantId}/category/{category}")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByCategory(
            @PathVariable String restaurantId,
            @PathVariable String category) {
        logger.info("Fetching menu items for restaurant ID: {} and category: {}", restaurantId, category);
        return ResponseEntity.ok(menuItemService.getMenuItemsByCategory(restaurantId, category));
    }

    // update the menu item stock amount
    @PutMapping("/{itemId}/amount")
    public ResponseEntity<String> updateMenuItemQuantity(
            @PathVariable String itemId, @RequestParam int newQuantity) {
        logger.info("Updating status for restaurant ID: {} to {}", itemId, newQuantity);
        try {
            String result = menuItemService.updateMenuItemQuantity(itemId, newQuantity);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("Error updating Item Quantity: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get the most selling menu items (first three)
    @GetMapping("/restaurant/{restaurantId}/top-selling")
    public ResponseEntity<List<MenuItemDto>> getTopSellingMenuItems(
            @PathVariable String restaurantId) {
        logger.info("Fetching top selling menu items for restaurant ID: {}", restaurantId);
        return ResponseEntity.ok(menuItemService.getTopSellingMenuItems(restaurantId));
    }
}