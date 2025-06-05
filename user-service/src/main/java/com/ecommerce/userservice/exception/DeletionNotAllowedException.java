package com.ecommerce.userservice.exception;

@SuppressWarnings("serial")
public class DeletionNotAllowedException extends RuntimeException{
	
	public DeletionNotAllowedException(String message) {
		super(message);
	}

}
