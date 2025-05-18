package com.example.email_service.controller;

import com.example.email_service.model.Email;
import com.example.email_service.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestBody Email email) {
        emailService.sendEmail(email.getTo(), email.getSubject(), email.getText());
        return "Email sending triggered!";
    }
}
