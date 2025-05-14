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

    //find and assign a driver to order
    @PostMapping("/assign")
    public ResponseEntity<?> assignDriver(@RequestBody OrderDTO orderData) {
        return deliveryService.assignDriver(orderData);
    }



//    @PostMapping("/assign")
//    public ResponseEntity<Void> assignDelivery(@RequestBody OrderDTO orderDetails) {
//        // Logic to assign a driver and update order status
//        Driver assignedDriver = deliveryService.assignDeliveryDriver(orderDetails);
//
//
//        return ResponseEntity.ok().build();
//    }

    //findDriverSessionByDriverId
    // public WebSocketSession getSessionByDriverId(String driverId) {
    //    return driverSessions.get(driverId);
    //}

}
