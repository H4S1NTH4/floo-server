package com.floo.payment_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;

    private String stripeSessionId;
    private String name;
    private String currency;
    private Long amount;
    private Long quantity;

    private PaymentStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
