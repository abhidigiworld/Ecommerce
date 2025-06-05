package com.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;
 
@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class OrderItemResponse {

    private Long orderItemId;

    private Long productId;

    private String productName;

    private Double productPrice;

    private Integer quantity;

    private Double subtotal;

}
 
