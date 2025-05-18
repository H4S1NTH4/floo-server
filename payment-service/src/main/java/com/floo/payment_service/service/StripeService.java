
package com.floo.payment_service.service;

import com.floo.payment_service.dto.ProductRequest;
import com.floo.payment_service.dto.StripeResponse;
import com.floo.payment_service.feign.EmailInterface;
import com.floo.payment_service.model.Email;
import com.floo.payment_service.model.Payment;
import com.floo.payment_service.model.PaymentStatus;
import com.floo.payment_service.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    private final PaymentRepository paymentRepository;

    public StripeService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public StripeResponse checkoutProducts(ProductRequest req) {
        Stripe.apiKey = secretKey;

        // 1) Create & save a Payment in PENDING state
        Payment payment = Payment.builder()
                .currency(req.getCurrency() != null ? req.getCurrency() : "usd")
                .name(req.getName())
                .amount(req.getAmount())
                .quantity(req.getQuantity())
                .status(PaymentStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        payment = paymentRepository.save(payment);

        // 2) Build Stripe Session params
        SessionCreateParams.LineItem.PriceData.ProductData pd =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(req.getName())
                        .build();

        long amountInPaise = payment.getAmount() * 100L;  // 38000 paise

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(payment.getCurrency())
                        .setUnitAmount(amountInPaise)
                        .setProductData(pd)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(payment.getQuantity())
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:3000/customer/payments/success")
                        .setCancelUrl("http://localhost:3000/customer/payments/fail")
                        .addLineItem(lineItem)
                        .build();

        // 3) Create Stripe Session and update Payment record
        try {
            Session session = Session.create(params);

            payment.setStripeSessionId(session.getId());
            payment.setUpdatedAt(Instant.now());
            paymentRepository.save(payment);


            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            // mark as failed
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(Instant.now());
            paymentRepository.save(payment);
            throw new RuntimeException("Stripe checkout failed", e);
        }
    }
}
