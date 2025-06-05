package com.ecommerce.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private Long userId;
    private Long productId;
    private String productName;
    private Double unitPrice;
    private Double productPrice;
    private String imageUrl;
    private Integer quantity;
    private Double totalPrice;
}
