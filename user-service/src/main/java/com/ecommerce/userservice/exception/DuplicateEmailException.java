package com.ecommerce.userservice.exception;

@SuppressWarnings("serial")
public class DuplicateEmailException extends Exception {
	public DuplicateEmailException(String message) {
		super(message);
	}
}
