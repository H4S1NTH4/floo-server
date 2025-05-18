package com.floo.payment_service.controller;

import com.floo.payment_service.model.Payment;
import com.floo.payment_service.model.PaymentStatus;
import com.floo.payment_service.repository.PaymentRepository;

import com.floo.payment_service.service.EmailService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @Autowired
    EmailService emailService;

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    private final PaymentRepository paymentRepository;

    public StripeWebhookController(PaymentRepository repo) {
        this.paymentRepository = repo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void handle(@RequestBody String payload,
                       @RequestHeader("Stripe-Signature") String sigHeader) {
        // 1) Verify signature
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid signature");
        }

        // 2) Only care about checkout.session.completed here
        if ("checkout.session.completed".equals(event.getType())) {
            // 3) Parse the raw JSON to get the session ID
            JsonObject data = JsonParser.parseString(payload)
                    .getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonObject("object");
            String sessionId = data.get("id").getAsString();

            // 4) Fetch the full Session from Stripe
            Stripe.apiKey = secretKey;
            Session session;
            try {
                session = Session.retrieve(sessionId);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve session");
            }

            // 5) Update your Payment record
            Payment payment = paymentRepository.findByStripeSessionId(session.getId());
            if (payment != null) {
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setUpdatedAt(Instant.now());
                paymentRepository.save(payment);
                emailService.sendSuccessEmail(payment.getId(), payment.getAmount().toString(), "Jayasekara M P S S");
            }

        }
        // (You can handle other event types here as needed.)
    }
}
