package com.floo.restaurant_service.feign;

import com.floo.restaurant_service.dto.PaymentConfirmation;
import com.floo.restaurant_service.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {
    @PostMapping("/api/v1/payments/confirm")
    void confirmPayment(@RequestBody PaymentConfirmation confirmation);

    @GetMapping("/api/v1/payments/order/{orderId}")
    Order.PaymentStatus getPaymentStatus(@PathVariable String orderId);
}
