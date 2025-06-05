package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.entity.OrderStatus;

import com.ecommerce.orderservice.entity.PaymentStatus;

import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;
 
import java.time.LocalDateTime;

import java.util.List;
 
@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class OrderResponse {

    private Long orderId;

    private Long userId;

    private String userName;

    private String userEmail;

    private Double totalPrice;

    private String shippingAddress;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private LocalDateTime orderDate;

    private List<OrderItemResponse> orderItems;

}
 
