package com.floo.order_service.controller;

import com.floo.order_service.dto.OrderStatusUpdateRequest;
import com.floo.order_service.model.Order;
import com.floo.order_service.model.OrderStatus;
import com.floo.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {

        return orderService.addOrder(order);

    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<Order>> getAllOrders() {
        // get all orders
        return orderService.getAllOrders();
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable String orderId, @RequestBody Order order) {
        // update an order
        return orderService.updateOrder(orderId, order);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
        // delete an order
        return orderService.deleteOrder(orderId);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId,
                                               @RequestBody OrderStatusUpdateRequest request) {
        // update order status
        return orderService.updateOrderStatus(orderId, request.getOrderStatus());
    }

//    @GetMapping("generate")
//    public ResponseEntity<String> generateOrders() {
//        // Logic to generate orders
//
//        return orderService.generateOrders();
//    }

    //generate orders
    //getOrders (order id)
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    // customer client
    //getOrders (customer id)
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable String customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }

    // restaurant service
    //getOrders (restaurant id)
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getOrdersByRestaurantId(@PathVariable String restaurantId) {
        return orderService.getOrdersByRestaurantId(restaurantId);
    }


    // admin client
    // delivery service
    //getOrders (order id, order status)
    @GetMapping("/{orderId}/status/{status}")
    public ResponseEntity<?> getOrderByIdAndStatus(@PathVariable String orderId, @PathVariable OrderStatus status) {
        return orderService.getOrderByIdAndStatus(orderId, status);
    }


    @GetMapping("/test")
    public String testOrderService() {
        return "Order Service is running!";
    }
}
