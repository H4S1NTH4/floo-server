package com.floo.payment_service.repository;

import com.floo.payment_service.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    // Optional helper:
    Payment findByStripeSessionId(String sessionId);
}
