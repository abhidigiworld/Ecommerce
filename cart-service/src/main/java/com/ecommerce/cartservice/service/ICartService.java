package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.dto.CartItemRequest;
import com.ecommerce.cartservice.dto.CartItemResponse;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.exception.CartItemNotFoundException;

public interface ICartService {
    CartItemResponse addOrUpdateCartItem(CartItemRequest cartItemRequest) throws CartItemNotFoundException;
    void removeCartItem(Long cartItemId) throws CartItemNotFoundException;
    CartResponse getCartByUserId(Long userId) throws CartItemNotFoundException;
    String clearCartByUserId(Long userId) throws CartItemNotFoundException;
}
