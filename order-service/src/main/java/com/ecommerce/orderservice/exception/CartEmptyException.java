package com.ecommerce.orderservice.exception;

@SuppressWarnings("serial")
public class CartEmptyException extends Exception {
    public CartEmptyException(String message) {
        super(message);
    }
}
