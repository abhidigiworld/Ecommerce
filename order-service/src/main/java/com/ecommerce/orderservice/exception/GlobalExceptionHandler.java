package com.ecommerce.orderservice.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.orderservice.client.LoggingClient;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
    private LoggingClient loggingClient;

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleOrderNotFoundException(OrderNotFoundException ex) {
        loggingClient.logMessage("ERROR", "OrderNotFoundException: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleUserNotFoundException(UserNotFoundException ex) {
        loggingClient.logMessage("ERROR", "UserNotFoundException: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ErrorInfo> handleCartEmptyException(CartEmptyException ex) {
        loggingClient.logMessage("ERROR", "CartEmptyException: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ErrorInfo> handleCartEmptyException(InvalidOrderStatusException ex) {
        loggingClient.logMessage("ERROR", "InvalidOrderStatusException: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleGenericException(Exception ex) {
        loggingClient.logMessage("ERROR", "Unexpected Exception: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
