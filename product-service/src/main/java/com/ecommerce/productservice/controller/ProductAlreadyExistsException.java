package com.ecommerce.productservice.controller;
@SuppressWarnings("serial")
public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
