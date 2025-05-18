package com.floo.restaurant_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    private String orderId;
    private String userId;
    private String comment;
    private int rating; // 1-5
    private LocalDateTime createdAt;
}