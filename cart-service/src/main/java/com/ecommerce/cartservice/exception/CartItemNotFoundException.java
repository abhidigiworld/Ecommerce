package com.ecommerce.cartservice.exception;

@SuppressWarnings("serial")
public class CartItemNotFoundException extends Exception {
	public CartItemNotFoundException(String message) {
        super(message);
    }
}
