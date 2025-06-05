package com.ecommerce.productservice.exception;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.productservice.client.LoggingClient;
import com.ecommerce.productservice.controller.ProductAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
    private LoggingClient loggingClient;

	@ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorInfo errorInfo = new ErrorInfo(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        loggingClient.logMessage("ERROR", "ProductNotFoundException: " + ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
        ErrorInfo errorInfo = new ErrorInfo(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        loggingClient.logMessage("ERROR", "ProductAlreadyExistsException: " + ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleGenericException(Exception ex) {
        ErrorInfo errorInfo = new ErrorInfo(
                "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
        loggingClient.logMessage("ERROR", "Generic Exception: " + ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

