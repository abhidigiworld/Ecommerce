package com.ecommerce.adminservice.exception;

@SuppressWarnings("serial")
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
