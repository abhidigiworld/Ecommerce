package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.CartItemRequest;
import com.ecommerce.cartservice.dto.CartItemResponse;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.exception.CartItemNotFoundException;
import com.ecommerce.cartservice.service.ICartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addOrUpdateCartItem(@RequestBody CartItemRequest cartItemRequest) throws CartItemNotFoundException {
        return ResponseEntity.ok(cartService.addOrUpdateCartItem(cartItemRequest));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) throws CartItemNotFoundException {
        cartService.removeCartItem(cartItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId) throws CartItemNotFoundException {
        return new ResponseEntity<>(cartService.getCartByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCartByUserId(@PathVariable Long userId) throws CartItemNotFoundException {
        return new ResponseEntity<>(cartService.clearCartByUserId(userId), HttpStatus.NO_CONTENT);
    }
}

 
