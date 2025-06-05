package com.ecommerce.adminservice.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import com.ecommerce.adminservice.client.LoggingClient;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LoggingClient loggingClient;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleUserNotFoundException(UserNotFoundException ex) {
        loggingClient.logMessage("ERROR", "UserNotFoundException: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleProductNotFoundException(ProductNotFoundException ex) {
        loggingClient.logMessage("ERROR", "ProductNotFoundException: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleOrderNotFoundException(OrderNotFoundException ex) {
        loggingClient.logMessage("ERROR", "OrderNotFoundException: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorInfo> handleFeignException(FeignException ex) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setLocalDateTime(LocalDateTime.now());;
        
        if(ex.status() == 404) {
            errorInfo.setMessage("Requested resource not found");
            errorInfo.setHttpStatus(HttpStatus.NOT_FOUND);
            loggingClient.logMessage("ERROR", "Resource not found: " + ex.getMessage());
        } else {
            errorInfo.setMessage("Service communication error");
            errorInfo.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            loggingClient.logMessage("ERROR", "Feign client error: " + ex.getMessage());
        }
        
        return new ResponseEntity<>(errorInfo, HttpStatus.valueOf(ex.status()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleGenericException(Exception ex) {
        loggingClient.logMessage("ERROR", "Unexpected Exception: " + ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
