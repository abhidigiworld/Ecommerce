package com.ecommerce.adminservice.exception;

@SuppressWarnings("serial")
public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
