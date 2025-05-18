package com.floo.payment_service.service;

import com.floo.payment_service.feign.EmailInterface;
import com.floo.payment_service.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    EmailInterface emailInterface;

    public void sendSuccessEmail(String paymentId, String amount, String customerName) {
        Email email = new Email();
        email.setTo("mpsjayasekara@gmail.com");
        email.setSubject("Payment Successful");
        email.setText("Payment Successful for paymentId: " + paymentId + " with amount: " + amount + " for customer: " + customerName + " ");
        emailInterface.sendEmail(email);
    }


}