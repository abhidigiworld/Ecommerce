package com.ecommerce.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private Long userId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String imageUrl;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}