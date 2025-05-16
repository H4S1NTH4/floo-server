package com.floo.restaurant_service.service;

import com.floo.restaurant_service.dto.*;
import com.floo.restaurant_service.feign.DeliveryServiceClient;
import com.floo.restaurant_service.feign.NotificationClient;
import com.floo.restaurant_service.model.*;
import com.floo.restaurant_service.repository.MenuItemRepository;
import com.floo.restaurant_service.repository.RestaurantRepository;
import com.floo.restaurant_service.utils.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private final DeliveryServiceClient deliveryServiceClient;
    private final NotificationClient notificationClient;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(RestaurantService.class);

    // create new restaurant
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

    // get my restaurant
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

    // get all restaurants by admin
    @Transactional(readOnly = true)
    public List<RestaurantDto> getAllRestaurants() {
        logger.info("Fetching all restaurants for admin view");
        return restaurantRepository.findAll().stream()
                .map(this::getRestaurantDto)
                .collect(Collectors.toList());
    }

    // update the restaurant
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

    // update restaurant availability status
    @Transactional
    public String updateRestaurantStatus(String id, Restaurant.RestaurantStatus status) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        restaurant.setStatus(status);
        restaurantRepository.save(restaurant);

        logger.info("Restaurant ID: {} status updated to {}", id, status);
        return String.format("Restaurant status updated to %s", status);
    }

    // get pending restaurant (admin)
    @Transactional(readOnly = true)
    public List<RestaurantDto> getPendingRestaurants() {
        return restaurantRepository.findByIsVerifiedFalse().stream()
                .map(this::getRestaurantDto)
                .collect(Collectors.toList());
    }

    // verify restaurant (admin)
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

    // restaurant to dto
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

    // menu item to dto
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

    // orders to dto
    private List<OrderDto> convertToOrderDtos(List<Order> orders) {
        return orders.stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }

    // order to dto
    private OrderDto convertToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .restaurantId(order.getRestaurantId())
                .userId(order.getUserId())
                .items(convertToOrderItemDtos(order.getItems()))
                .totalPrice(BigDecimal.valueOf(order.getTotalPrice()))
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .deliveryAddress(order.getDeliveryAddress())
                .build();
    }

    // order items to dto
    private List<OrderItemDto> convertToOrderItemDtos(List<OrderItem> items) {
        return items.stream()
                .map(item -> OrderItemDto.builder()
                        .menuItemId(item.getMenuItemId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    // order item to dto
    private OrderItemDto convertToOrderItemDto(OrderItem item) {
        return OrderItemDto.builder()
                .menuItemId(item.getMenuItemId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

    // update order status
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

        // Notify customer on all status changes
        notificationClient.sendNotification(
                NotificationRequest.builder()
                        .receiverUsername(order.getUserId())
                        .title("Order Status Update")
                        .message(String.format("Your order %s is now %s", orderId, status))
                        .build()
        );

        // Special handling for READY_FOR_PICKUP
        if (status == Order.OrderStatus.READY_FOR_PICKUP) {
            deliveryServiceClient.notifyDeliveryReady(order);

            // Also notify restaurant owner
            notificationClient.sendNotification(
                    NotificationRequest.builder()
                            .receiverUsername(getRestaurantOwner(restaurantId))
                            .title("Order Ready for Pickup")
                            .message(String.format("Order %s is ready for pickup", orderId))
                            .build()
            );
        }

        if (status == Order.OrderStatus.PICKED_UP) {
            restaurant.getActiveOrders().remove(order);
            restaurant.getPastOrders().add(order);
        }

        restaurantRepository.save(restaurant);
        return "Order status updated successfully";
        }

    // delete a restaurant
    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(id);
    }

    // get daily income
    public DailyIncomeResponse getDailyIncome(String restaurantId, LocalDate date) {
        List<Order> orders = restaurantRepository.findById(restaurantId)
                .map(r -> r.getPastOrders().stream()
                        .filter(o -> o.getUpdatedAt().toLocalDate().equals(date))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        double total = orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        return DailyIncomeResponse.builder()
                .date(date)
                .totalIncome(total)
                .orderCount(orders.size())
                .build();
    }

    // get total income
    public TotalIncomeResponse getTotalIncome(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        double total = restaurant.getPastOrders().stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        return TotalIncomeResponse.builder()
                .totalIncome(total)
                .orderCount(restaurant.getPastOrders().size())
                .build();
    }

    // add feedback
    public void addFeedback(String restaurantId, FeedbackRequest feedback) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Feedback newFeedback = Feedback.builder()
                .orderId(feedback.getOrderId())
                .userId(feedback.getUserId())
                .comment(feedback.getComment())
                .rating(feedback.getRating())
                .createdAt(LocalDateTime.now())
                .build();

        restaurant.getFeedbacks().add(newFeedback);
        restaurantRepository.save(restaurant);

        // Update restaurant rating
        double averageRating = restaurant.getFeedbacks().stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(restaurant.getRating() != null ? restaurant.getRating() : 0.0);

        restaurant.setRating(averageRating);
        restaurantRepository.save(restaurant);
    }

    // active orders (preparing to ready to be picked up)
    public List<OrderDto> getInProgressOrders(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(r -> r.getActiveOrders().stream()
                        .filter(o -> o.getStatus() != Order.OrderStatus.RECEIVED)
                        .filter(o -> o.getStatus() != Order.OrderStatus.READY_FOR_PICKUP)
                        .map(this::convertToOrderDto)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    // get completed orders (order status = picked up)
    public List<OrderDto> getCompletedOrders(String restaurantId, LocalDate date) {
        return restaurantRepository.findById(restaurantId)
                .map(r -> r.getPastOrders().stream()
                        .filter(o -> date == null || o.getUpdatedAt().toLocalDate().equals(date))
                        .map(this::convertToOrderDto)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    // view an order
    public OrderDto getOrder(String restaurantId, String orderId) {
        return restaurantRepository.findById(restaurantId)
                .flatMap(r -> r.getActiveOrders().stream()
                        .filter(o -> o.getOrderId().equals(orderId))
                        .findFirst()
                        .map(this::convertToOrderDto))
                .or(() -> restaurantRepository.findById(restaurantId)
                        .flatMap(r -> r.getPastOrders().stream()
                                .filter(o -> o.getOrderId().equals(orderId))
                                .findFirst()
                                .map(this::convertToOrderDto)))
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // get incoming orders
    public List<OrderDto> getActiveOrders(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant -> restaurant.getActiveOrders().stream()
                        .map(this::convertToOrderDto)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    // adding order to restaurant
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

    // check if order exists
    public boolean orderExists(String restaurantId, String orderId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant ->
                        restaurant.getActiveOrders().stream().anyMatch(o -> o.getOrderId().equals(orderId)) ||
                                restaurant.getPastOrders().stream().anyMatch(o -> o.getOrderId().equals(orderId))
                )
                .orElse(false);
    }

    // update order payment status
    @Transactional
    public void updateOrderPaymentStatus(String restaurantId, String orderId, Order.PaymentStatus status) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Optional<Order> orderOpt = restaurant.getActiveOrders().stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst();

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setPaymentStatus(status);
            restaurantRepository.save(restaurant);
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    // get restaurant owner
    public String getRestaurantOwner(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(Restaurant::getOwner)
                .map(OwnerInfo::getUsername)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
}