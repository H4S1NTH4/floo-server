package com.floo.order_service.service;

import com.floo.order_service.dto.OrderAddResponse;
import com.floo.order_service.dto.OrderNotificationDto;
import com.floo.order_service.dto.OrderUpdateResponse;
import com.floo.order_service.feign.DeliveryInterface;
import com.floo.order_service.feign.RestaurantInterface;
import com.floo.order_service.model.Order;
import com.floo.order_service.model.OrderStatus;
import com.floo.order_service.model.StatusChange;
import com.floo.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DeliveryInterface deliveryInterface;

    @Autowired
    private RestaurantInterface restaurantInterface;


    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            System.out.println("Fetching all orders");
            return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> addOrder(Order order) {
        try {

            System.out.println("Adding order: " + order);

            // Set initial status
            order.setOrderStatus(OrderStatus.PENDING);

            // Add to status history
            order.setStatusHistory(new ArrayList<>());
            order.getStatusHistory().add(new StatusChange(OrderStatus.PENDING, System.currentTimeMillis()));

            // Save order to DB
            Order savedOrder = orderRepository.save(order);

//            // Notify restaurant service
//            OrderNotificationDto notification = new OrderNotificationDto(
//                    savedOrder.getId(),
//                    savedOrder.getOrderStatus().name(),
//                    savedOrder.getCustomerId(),
//                    savedOrder.getRestaurantId(),
//                    savedOrder.getDeliveryAddress(),
//                    System.currentTimeMillis()
//            );
//
//            restaurantInterface.notifyRestaurant(notification);

            return new ResponseEntity<>(new OrderAddResponse("Order created and restaurant notified successfully", order), HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Order creation failed", HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> updateOrder(String orderId, Order updatedOrder) {
        try {
            // Find the existing order by ID
            Order existingOrder = orderRepository.findById(orderId).orElse(null);

            if (existingOrder == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            // Set the ID to ensure MongoDB updates the correct document
            updatedOrder.setId(orderId);

            // Save the updated order
            Order savedOrder = orderRepository.save(updatedOrder);

            return new ResponseEntity<>(new OrderUpdateResponse("Order updated successfully", savedOrder), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while updating the order", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteOrder(String orderId) {
        try {
            // Check if order exists
            if (orderRepository.existsById(orderId)) {
                // Delete the order
                orderRepository.deleteById(orderId);
                return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while deleting the order", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateOrderStatus(String orderId, OrderStatus newStatus) {
        try {
            Order existingOrder = orderRepository.findById(orderId).orElse(null);
            if (existingOrder == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            // Track timeline
            List<StatusChange> history = existingOrder.getStatusHistory();
            if (history == null) history = new ArrayList<>();
            history.add(new StatusChange(newStatus, System.currentTimeMillis()));
            existingOrder.setStatusHistory(history);

            existingOrder.setOrderStatus(newStatus);
            Order savedOrder = orderRepository.save(existingOrder);

            // If status is READY, notify delivery service
//            if (newStatus == OrderStatus.READY) {
//                try {
//                    ResponseEntity<String> deliveryResponse = deliveryInterface.testDeliveryService();
//                    // Optional: Handle delivery service response if needed
//                } catch (Exception e) {
//                    // Log error but continue with status update
//                    System.err.println("Failed to notify delivery service: " + e.getMessage());
//                    // Consider implementing retry logic or queue mechanism
//                }
//            }

            // Create notification payload
            OrderNotificationDto notification = new OrderNotificationDto(
                    savedOrder.getId(),
                    newStatus.name(),
                    savedOrder.getCustomerId(),
                    savedOrder.getRestaurantId(),
                    savedOrder.getDeliveryAddress(),
                    System.currentTimeMillis()
            );

//            // Notify restaurant if relevant
//            if (newStatus == OrderStatus.PENDING ||
//                    newStatus == OrderStatus.ACCEPTED ||
//                    newStatus == OrderStatus.DENIED ||
//                    newStatus == OrderStatus.PREPARING ||
//                    newStatus == OrderStatus.READY) {
//                restaurantInterface.notifyRestaurant(notification);
//            }

//            // Notify delivery service if relevant
//            if (newStatus == OrderStatus.ACCEPTED ||
//                    newStatus == OrderStatus.READY ||
//                    newStatus == OrderStatus.PICKED_UP) {
//                deliveryInterface.notifyDelivery(notification);
//            }

            return new ResponseEntity<>(
                    new OrderUpdateResponse("Order status updated successfully", savedOrder),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update order status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getOrderById(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            return new ResponseEntity<>(orderOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Order>> getOrdersByCustomerId(String customerId) {
        try {
            List<Order> orders = orderRepository.findByCustomerId(customerId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Order>> getOrdersByRestaurantId(String restaurantId) {
        try {
            List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getOrderByIdAndStatus(String orderId, OrderStatus status) {
        try {
            Order order = orderRepository.findByIdAndOrderStatus(orderId, status);
            if (order != null) {
                return new ResponseEntity<>(order, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Order not found with given status", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error fetching order", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
