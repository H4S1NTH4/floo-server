package com.floo.restaurant_service.controller;

import com.floo.restaurant_service.dto.MenuItemDto;
import com.floo.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/menuitem")
@RequiredArgsConstructor
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addMenuItem(@RequestBody MenuItemDto menuItemDto) {
        return menuItemService.addMenuItem(menuItemDto);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MenuItemDto> getMenuItemsByRestaurant(@PathVariable String restaurantId) {
        return menuItemService.getAllMenuItems(restaurantId);
    }

    @PutMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public String updateMenuItem(@PathVariable String itemId,
                                 @RequestBody MenuItemDto menuItemDto) {
        return menuItemService.updateMenuItem(itemId, menuItemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteMenuItem(@PathVariable String itemId) {
        return menuItemService.deleteMenuItem(itemId);
    }

    @PutMapping("/quantity")
    @ResponseStatus(HttpStatus.OK)
    public void updateMenuItemQuantity(@RequestParam List<String> menuItemIds,
                                       @RequestParam List<Integer> orderQuantities) {
        menuItemService.updateMenuItemQuantity(menuItemIds, orderQuantities);
    }
}