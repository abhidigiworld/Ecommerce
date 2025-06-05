package com.ecommerce.adminservice.dto;

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

public class OrderClientResponse {

    private Long orderId;

    private Long userId;

    private String userName;

    private String userEmail;

    private Double totalPrice;

    private String shippingAddress;

    private String orderStatus;

    private String paymentStatus;

    private LocalDateTime orderDate;

    private List<OrderItemClientResponse> orderItems;
 
    @Data

    @NoArgsConstructor

    @AllArgsConstructor

    @Builder

    public static class OrderItemClientResponse {

        private Long orderItemId;

        private Long productId;

        private String productName;

        private Double productPrice;

        private Integer quantity;

        private Double subtotal;

    }

}
 
