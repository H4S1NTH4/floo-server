package com.floo.delivery_service.dto;

import com.floo.delivery_service.entity.GeoLocation;

import java.util.List;

public class OrderDTO {
    private Long id;
    private Long customerId;
    private String customerName; // optional but useful for delivery
    private GeoLocation deliveryLocation;
    private GeoLocation pickupLocation;
    private String status; // e.g., PENDING, PREPARING, OUT_FOR_DELIVERY, DELIVERED
    private Double totalAmount;
    private String createdAt;

    private List<OrderItemDTO> items; // list of food items

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public GeoLocation getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(GeoLocation deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public GeoLocation getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(GeoLocation pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
}

