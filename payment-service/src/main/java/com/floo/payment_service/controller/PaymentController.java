
package com.floo.payment_service.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {
    @GetMapping("/test")
    public String testPaymentService() {
        return "Payment Service is running!";
    }
}
