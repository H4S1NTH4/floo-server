package com.floo.delivery_service.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryController {

    @GetMapping("/test")
    public String testDeliveryService() {
        return "Delivery Service is running!";
    }
}
