package com.floo.delivery_service.client;

import com.floo.delivery_service.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name= "order-service")
public interface OrderClient {

    @GetMapping("/api/v1/order/{id}") // path must match order-service controller
    OrderDTO getOrderById(@PathVariable("id") Long orderId);

    //PUT req to update order status
    //@PutMapping("/api/v1/order/{id}")







}
