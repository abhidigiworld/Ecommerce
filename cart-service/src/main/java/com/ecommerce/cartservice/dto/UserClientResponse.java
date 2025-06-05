package com.ecommerce.cartservice.dto;

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
    private String paymentDetails;
    private boolean isDeleted;
}
