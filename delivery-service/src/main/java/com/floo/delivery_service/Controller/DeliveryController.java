package com.floo.delivery_service.Controller;

import com.floo.delivery_service.dto.OrderDTO;
import com.floo.delivery_service.entity.Driver;
import com.floo.delivery_service.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryController {

    @GetMapping("/test")
    public String testDeliveryService() {
        return "Delivery Service is running!";
    }


    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/{orderId}")
//    @ResponseStatus(HttpStatus.CREATED)
    public String deliverOrder(@PathVariable String orderId) {
        return deliveryService.deliverOrder(orderId);
    }

    public void notifyCustomer() {
        deliveryService.notifyCustomer();
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignDelivery(@RequestBody OrderDTO orderDetails) {
        // Logic to assign a driver and update order status
        Driver assignedDriver = deliveryService.assignDeliveryDriver(orderDetails);


        return ResponseEntity.ok().build();
    }


}
