package com.ecommerce.userservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private String userName;
    private String userEmail;
    private Double totalPrice;
    private String shippingAddress;
    private String orderStatus;
    private String paymentStatus;
    private String orderDate;
    private List<OrderItemResponse> orderItems;
}