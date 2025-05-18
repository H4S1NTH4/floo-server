package com.floo.restaurant_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;
    private String name;
    private String description;
    private Address address;
    private List<Long> contactInfo;
    private Double rating;
    private OwnerInfo owner;
    private boolean isAvailable = true;
    private boolean isVerified = false;
    private List<Feedback> feedbacks = new ArrayList<>();

    @Builder.Default
    private RestaurantStatus status = RestaurantStatus.OPEN; // OPEN, CLOSED, BUSY

    private List<Order> activeOrders = new ArrayList<>();
    private List<Order> pastOrders = new ArrayList<>();

    // Enum for restaurant status
    public enum RestaurantStatus {
        OPEN, CLOSED, BUSY
    }
}
