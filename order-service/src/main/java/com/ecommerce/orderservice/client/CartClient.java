package com.ecommerce.orderservice.client;
 
import com.ecommerce.orderservice.dto.CartClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
@FeignClient(name = "cart-service")
public interface CartClient {
    @GetMapping("/api/cart/{userId}")
    CartClientResponse getCartByUserId(@PathVariable("userId") Long userId);
 
    @DeleteMapping("/api/cart/clear/{userId}")
    String clearCartByUserId(@PathVariable("userId") Long userId);
}