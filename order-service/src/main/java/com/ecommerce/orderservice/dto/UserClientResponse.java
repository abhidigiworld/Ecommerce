package com.ecommerce.orderservice.dto;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserClientResponse {
    private Long userId;
    private String name;
    private String email;
    private String shippingAddress;
}