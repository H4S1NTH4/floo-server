package com.floo.payment_service.feign;

import com.floo.payment_service.model.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="EMAIL-SERVICE")
public interface EmailInterface {
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody Email email);
}
