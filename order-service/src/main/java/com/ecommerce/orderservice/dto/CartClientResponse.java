package com.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartClientResponse {
   private Long userId;
   private List<CartItemClientResponse> items;
   private Double cartTotal;

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @Builder
   public static class CartItemClientResponse {
       private Long cartItemId;
       private Long productId;
       private Integer quantity;
       private Double unitPrice;
       private Double totalPrice;
   }
}
