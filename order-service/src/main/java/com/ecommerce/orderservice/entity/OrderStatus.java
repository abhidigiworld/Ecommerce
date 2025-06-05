package com.ecommerce.orderservice.entity;
 
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED;
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED ;
    }
}