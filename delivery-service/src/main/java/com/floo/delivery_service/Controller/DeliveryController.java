package com.floo.delivery_service.Controller;

import com.floo.delivery_service.dto.OrderDTO;
import com.floo.delivery_service.entity.Driver;
import com.floo.delivery_service.entity.DriverStatus;
import com.floo.delivery_service.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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


  //get all drivers
    @PostMapping("/drivers")
    public ResponseEntity<?> createDriver(@RequestBody Driver driver) {
        return deliveryService.createDriver(driver);
    }

    //Updates the status of an existing driver.
    @PutMapping("/drivers/{driverId}/status")
    public ResponseEntity<?> updateDriverStatus(@PathVariable String driverId, @RequestBody Map<String, String> payload) {
        String statusStr = payload.get("status");
        if (statusStr == null) {
            return ResponseEntity.badRequest().body("Status field is required in the payload.");
        }
        try {
            DriverStatus status = DriverStatus.valueOf(statusStr.toUpperCase());
            return deliveryService.updateDriverStatus(driverId, status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value: " + statusStr +
                    ". Valid statuses are: " + String.join(", ", DriverStatus.ONLINE.name(),
                    DriverStatus.OFFLINE.name(), DriverStatus.DELIVERY.name()));
        }
    }

    //Updates the information of an existing driver.
    @PutMapping("/drivers/{driverId}")
    public ResponseEntity<?> updateDriverInfo(@PathVariable String driverId, @RequestBody Driver driverDetails) {
        return deliveryService.updateDriverInfo(driverId, driverDetails);
    }


     // Deletes driver by ID.
    @DeleteMapping("/drivers/{driverId}")
    public ResponseEntity<?> deleteDriver(@PathVariable String driverId) {
        return deliveryService.deleteDriver(driverId);
    }


     //Gets a driver by ID.
    @GetMapping("/drivers/{driverId}")
    public ResponseEntity<?> getDriverById(@PathVariable String driverId) {
        return deliveryService.getDriverById(driverId);
    }


     //Gets all drivers.
    @GetMapping("/drivers")
    public ResponseEntity<?> getAllDrivers() {
        return deliveryService.getAllDrivers();
    }
}
