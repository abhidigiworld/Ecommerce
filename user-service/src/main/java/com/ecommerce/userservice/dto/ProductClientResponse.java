package com.ecommerce.userservice.dto;

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

    private String description;

    private Double price;

    private String category;

    private String imageUrl;

    private Integer stockQuantity;

}