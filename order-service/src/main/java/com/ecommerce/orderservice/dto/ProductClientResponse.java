package com.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;
 
@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class ProductClientResponse {

    private Long productId;

    private String name;

    private Double price;

    private Integer stockQuantity;

}
 
