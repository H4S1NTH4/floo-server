package com.floo.restaurant_service.service;

import com.floo.restaurant_service.dto.*;
import com.floo.restaurant_service.feign.DeliveryServiceClient;
import com.floo.restaurant_service.feign.OrderServiceClient;
import com.floo.restaurant_service.model.MenuItem;
import com.floo.restaurant_service.model.Order;
import com.floo.restaurant_service.model.OrderItem;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.repository.MenuItemRepository;
import com.floo.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final OrderServiceClient orderServiceClient;
    private final DeliveryServiceClient deliveryServiceClient;

    private final String ROLE = "RESTAURANT_OWNER";
    private final String ADMIN_ROLE = "ADMIN";

    public String addRestaurant(RestaurantRequest request) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .contactInfo(request.getContactInfo())
                .address(request.getAddress())
                .rating(request.getRating())
                .isAvailable(true)
                .isVerified(false)
                .build();

        Restaurant saved = restaurantRepository.save(restaurant);
        return "Restaurant with id: " + saved.getId() + " added successfully";
    }

    public RestaurantResponse getRestaurant(String id) {
        return restaurantRepository.findById(id)
                .map(restaurant -> RestaurantResponse.builder()
                        .restaurantDto(getRestaurantDto(restaurant))
                        .responseCode(200)
                        .msg("Success")
                        .build())
                .orElse(RestaurantResponse.builder()
                        .responseCode(404)
                        .msg("Restaurant not found")
                        .build());
    }

    @Transactional(readOnly = true)
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::getRestaurantDto)
                .collect(Collectors.toList());
    }

    public String updateRestaurant(String id, RestaurantRequest request) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(request.getName());
                    restaurant.setDescription(request.getDescription());
                    restaurant.setAddress(request.getAddress());
                    restaurant.setContactInfo(request.getContactInfo());
                    restaurant.setRating(request.getRating());
                    restaurantRepository.save(restaurant);
                    return "Restaurant updated successfully";
                })
                .orElse("Restaurant not found");
    }

    public String updateAvailability(String id, boolean isAvailable) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setAvailable(isAvailable);
                    restaurantRepository.save(restaurant);
                    return "Availability updated to: " + isAvailable;
                })
                .orElse("Restaurant not found");
    }

    @Transactional(readOnly = true)
    public List<RestaurantDto> getPendingRestaurants() {
        return restaurantRepository.findByIsVerifiedFalse().stream()
                .map(this::getRestaurantDto)
                .collect(Collectors.toList());
    }

    public String verifyRestaurant(String id, boolean isApproved) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    if (isApproved) {
                        restaurant.setVerified(true);
                        restaurantRepository.save(restaurant);
                        return "Restaurant approved successfully";
                    } else {
                        restaurantRepository.delete(restaurant);
                        return "Restaurant rejected and removed";
                    }
                })
                .orElse("Restaurant not found");
    }

    private RestaurantDto getRestaurantDto(Restaurant restaurant) {
        RestaurantDto dto = RestaurantDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .contactInfo(restaurant.getContactInfo())
                .rating(restaurant.getRating())
                .isAvailable(restaurant.isAvailable())
                .isVerified(restaurant.isVerified())
                .status(Restaurant.RestaurantStatus.valueOf(restaurant.getStatus().toString()))
                .activeOrders(convertToOrderDtos(restaurant.getActiveOrders()))
                .pastOrders(convertToOrderDtos(restaurant.getPastOrders()))
                .build();

        List<MenuItemDto> menuItemDtos = menuItemRepository.findByRestaurantId(restaurant.getId())
                .stream()
                .map(this::convertToMenuItemDto)
                .collect(Collectors.toList());

        dto.setMenuItems(menuItemDtos);
        return dto;
    }

    private MenuItemDto convertToMenuItemDto(MenuItem menuItem) {
        return MenuItemDto.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .icon(menuItem.getIcon())
                .quantity(menuItem.getQuantity())
                .restaurantId(menuItem.getRestaurantId())
                .build();
    }

    private List<OrderDto> convertToOrderDtos(List<Order> orders) {
        return orders.stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }

    private OrderDto convertToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .restaurantId(order.getRestaurantId())
                .userId(order.getUserId())
                .items(convertToOrderItemDtos(order.getItems()))
                .totalPrice(BigDecimal.valueOf(order.getTotalPrice()))
                .status(order.getStatus().toString())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .deliveryAddress(order.getDeliveryAddress())
                .build();
    }

    private List<OrderItemDto> convertToOrderItemDtos(List<OrderItem> items) {
        return items.stream()
                .map(this::convertToOrderItemDto)
                .collect(Collectors.toList());
    }

    private OrderItemDto convertToOrderItemDto(OrderItem item) {
        return OrderItemDto.builder()
                .menuItemId(item.getMenuItemId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

    public String updateRestaurantStatus(String id, Restaurant.RestaurantStatus status) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setStatus(status);
                    restaurantRepository.save(restaurant);
                    return "Restaurant status updated to: " + status;
                })
                .orElse("Restaurant not found");
    }

    @Transactional
    public String addOrderToRestaurant(String restaurantId, Order order) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        order.setStatus(Order.OrderStatus.RECEIVED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        restaurant.getActiveOrders().add(order);
        restaurantRepository.save(restaurant);

        return "Order added to restaurant successfully";
    }

    @Transactional
    public String updateOrderStatus(String restaurantId, String orderId, Order.OrderStatus status) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Order order = restaurant.getActiveOrders().stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        if (status == Order.OrderStatus.READY_FOR_PICKUP) {
            deliveryServiceClient.notifyDeliveryReady(order);
        }

        if (status == Order.OrderStatus.PICKED_UP) {
            restaurant.getActiveOrders().remove(order);
            restaurant.getPastOrders().add(order);
        }

        restaurantRepository.save(restaurant);
        return "Order status updated successfully";
    }

    public List<Order> getActiveOrders(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(Restaurant::getActiveOrders)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    public List<Order> getPastOrders(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(Restaurant::getPastOrders)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
}