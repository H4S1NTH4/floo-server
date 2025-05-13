package com.floo.restaurant_service.service;


import com.floo.restaurant_service.dto.MenuItemDto;
import com.floo.restaurant_service.model.MenuItem;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.repository.MenuItemRepository;
import com.floo.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

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

    public String deleteMenuItem(String itemId) {
        return menuItemRepository.findById(itemId)
                .map(menuItem -> {
                    menuItemRepository.delete(menuItem);
                    return "Menu item deleted successfully";
                })
                .orElse("Menu item not found");
    }

    public void updateMenuItemQuantity(List<String> menuItemIds, List<Integer> orderQuantities) {
        if (menuItemIds.size() != orderQuantities.size()) {
            throw new IllegalArgumentException("MenuItemIds and Quantities must be of same size");
        }

        for (int i = 0; i < menuItemIds.size(); i++) {
            String menuItemId = menuItemIds.get(i);
            Integer quantity = orderQuantities.get(i);

            menuItemRepository.findById(menuItemId).ifPresent(menuItem -> {
                int newQuantity = menuItem.getQuantity() - quantity;
                menuItem.setQuantity(Math.max(newQuantity, 0));
                menuItemRepository.save(menuItem);
            });
        }
    }

    public void updateStock(String menuItemId, int quantityChange) {
        menuItemRepository.findById(menuItemId).ifPresent(menuItem -> {
            int newQuantity = menuItem.getQuantity() + quantityChange;
            menuItem.setQuantity(Math.max(newQuantity, 0));
            menuItemRepository.save(menuItem);
        });
    }

    public boolean isItemAvailable(String menuItemId, int quantity) {
        return menuItemRepository.findById(menuItemId)
                .map(item -> item.getQuantity() >= quantity)
                .orElse(false);
    }
}