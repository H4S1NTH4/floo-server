package com.floo.order_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.floo.order_service.model.Order;


@FeignClient("DELIVERY-SERVICE")
public interface DeliveryInterface {
//    @GetMapping("/api/v1/delivery/test")
//    public String testDeliveryService();

    @PostMapping("/api/v1/delivery/create")
    ResponseEntity<?> createDeliveryTask(@RequestBody Order order);

    @PostMapping("/api/v1/delivery/test")
    public ResponseEntity<String> testDeliveryService();

}
