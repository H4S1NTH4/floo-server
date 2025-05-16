package com.floo.restaurant_service.service;


import com.floo.restaurant_service.controller.RestaurantController;
import com.floo.restaurant_service.dto.MenuItemDto;
import com.floo.restaurant_service.model.MenuItem;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.repository.MenuItemRepository;
import com.floo.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(MenuItemService.class);

    // add a menu item
    public String addMenuItem(MenuItemDto menuItemDto) {
        MenuItem menuItem = MenuItem.builder()
                .name(menuItemDto.getName())
                .description(menuItemDto.getDescription())
                .quantity(menuItemDto.getQuantity())
                .icon(menuItemDto.getIcon())
                .price(menuItemDto.getPrice())
                .restaurantId(menuItemDto.getRestaurantId())
                .build();

        MenuItem saved = menuItemRepository.save(menuItem);
        return "Menu item added with id: " + saved.getId();
    }

    // get all menu items
    @Transactional(readOnly = true)
    public List<MenuItemDto> getAllMenuItems(String restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(menuItem -> MenuItemDto.builder()
                        .id(menuItem.getId())
                        .name(menuItem.getName())
                        .description(menuItem.getDescription())
                        .icon(menuItem.getIcon())
                        .quantity(menuItem.getQuantity())
                        .price(menuItem.getPrice())
                        .restaurantId(menuItem.getRestaurantId())
                        .build())
                .toList();
    }

    // get a menu item by id
    public MenuItemDto getMenuItemById(String itemId) {
        return menuItemRepository.findById(itemId)
                .map(menuItem -> MenuItemDto.builder()
                        .id(menuItem.getId())
                        .name(menuItem.getName())
                        .description(menuItem.getDescription())
                        .icon(menuItem.getIcon())
                        .quantity(menuItem.getQuantity())
                        .price(menuItem.getPrice())
                        .restaurantId(menuItem.getRestaurantId())
                        .category(menuItem.getCategory())
                        .build())
                .orElse(null);
    }

    // get menu items by category
    public List<MenuItemDto> getMenuItemsByCategory(String restaurantId, String category) {
        return menuItemRepository.findByRestaurantIdAndCategory(restaurantId, category).stream()
                .map(menuItem -> MenuItemDto.builder()
                        .id(menuItem.getId())
                        .name(menuItem.getName())
                        .description(menuItem.getDescription())
                        .icon(menuItem.getIcon())
                        .quantity(menuItem.getQuantity())
                        .price(menuItem.getPrice())
                        .restaurantId(menuItem.getRestaurantId())
                        .category(menuItem.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    // get top selling menu items
    public List<MenuItemDto> getTopSellingMenuItems(String restaurantId) {
        return menuItemRepository.findByRestaurantIdOrderByTotalSalesDesc(restaurantId).stream()
                .limit(3)
                .map(menuItem -> MenuItemDto.builder()
                        .id(menuItem.getId())
                        .name(menuItem.getName())
                        .description(menuItem.getDescription())
                        .icon(menuItem.getIcon())
                        .quantity(menuItem.getQuantity())
                        .price(menuItem.getPrice())
                        .restaurantId(menuItem.getRestaurantId())
                        .category(menuItem.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    // update a menu item
    public String updateMenuItem(String itemId, MenuItemDto menuItemDto) {
        return menuItemRepository.findById(itemId)
                .map(existingItem -> {
                    existingItem.setName(menuItemDto.getName());
                    existingItem.setDescription(menuItemDto.getDescription());
                    existingItem.setIcon(menuItemDto.getIcon());
                    existingItem.setPrice(menuItemDto.getPrice());
                    existingItem.setQuantity(menuItemDto.getQuantity());
                    menuItemRepository.save(existingItem);
                    return "Menu item updated successfully";
                })
                .orElse("Menu item not found");
    }

    // delete a menu item
    public String deleteMenuItem(String itemId) {
        return menuItemRepository.findById(itemId)
                .map(menuItem -> {
                    menuItemRepository.delete(menuItem);
                    return "Menu item deleted successfully";
                })
                .orElse("Menu item not found");
    }

    // vary menu item quantity
    @Transactional
    public String updateMenuItemQuantity(String itemId, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        MenuItem menuItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        menuItem.setQuantity(newQuantity);
        menuItemRepository.save(menuItem);

        logger.info("Menu item ID: {} quantity updated to {}", itemId, newQuantity);
        return String.format("Menu item quantity updated to %d", newQuantity);
    }
}