package com.ecommerce.orderservice.exception;

@SuppressWarnings("serial")
public class InvalidOrderStatusException extends Exception {
	public InvalidOrderStatusException(String message) {
		super(message);
	}
}
