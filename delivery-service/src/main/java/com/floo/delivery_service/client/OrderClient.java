package com.floo.delivery_service.client;

import com.floo.delivery_service.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name= "order-service")
public interface OrderClient {

    @GetMapping("/api/v1/order/{id}") // path must match order-service controller
    ResponseEntity<SpringDataJaxb.OrderDto> getOrderById(@PathVariable("id") Long orderId);

    @PutMapping("/update/{orderId}")
    ResponseEntity<?> updateOrder(@PathVariable("orderId") String orderId, @RequestBody SpringDataJaxb.OrderDto order);

    @PatchMapping("/{orderId}/status")
    ResponseEntity<?> updateOrderStatus(@PathVariable("orderId") String orderId,
                                        @RequestBody SpringDataJaxb.OrderDto request);

    @PatchMapping("/{orderId}/driver/{driverId}")
    ResponseEntity<?> assignDriverToOrder(@PathVariable("orderId") String orderId,
                                          @PathVariable("orderId") String driverId,
                                        @RequestBody SpringDataJaxb.OrderDto request);




}
