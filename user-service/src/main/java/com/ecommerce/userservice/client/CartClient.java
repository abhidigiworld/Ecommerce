package com.ecommerce.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecommerce.userservice.dto.CartItemRequest;
import com.ecommerce.userservice.dto.CartItemResponse;
import com.ecommerce.userservice.dto.CartResponse;

@FeignClient(name = "cart-service")
public interface CartClient {
    @PostMapping("/api/cart/add")
    ResponseEntity<CartItemResponse> addOrUpdateCartItem(@RequestBody CartItemRequest request);

    @DeleteMapping("/api/cart/remove/{cartItemId}")
    ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId);

    @GetMapping("/api/cart/{userId}")
    ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId);

    @DeleteMapping("/api/cart/clear/{userId}")
    ResponseEntity<String> clearCartByUserId(@PathVariable Long userId);
}