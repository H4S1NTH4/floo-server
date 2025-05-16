package com.floo.order_service.model;

public enum OrderStatus {
    PENDING,           // Order created, awaiting payment
    ACCEPTED,          // Order accepted by restaurant
    DENIED,            // Order denied by restaurant
    PREPARING,         // Order is being prepared
    READY,             // Order is ready for pickup or delivery
    ASSIGNED,          // Driver has been assigned to the order
    PICKED_UP,         // Order has left for delivery
    DELIVERING,        // Currently being delivered
    DELIVERED,         // Items have been delivered
    COMPLETED,         // Payment completed and process finalized
    CANCELLED          // Order was cancelled
}
