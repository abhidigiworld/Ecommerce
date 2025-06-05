package com.ecommerce.cartservice.exception;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.cartservice.client.LoggingClient;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
    private LoggingClient loggingClient;

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleCartItemNotFoundException(CartItemNotFoundException ex) {
        ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        loggingClient.logMessage("ERROR", "CartItemNotFoundException: " + ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleGenericException(Exception ex) {
        ErrorInfo errorInfo = new ErrorInfo("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        loggingClient.logMessage("ERROR", "Generic Exception: " + ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
