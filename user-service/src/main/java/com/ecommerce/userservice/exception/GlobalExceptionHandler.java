package com.ecommerce.userservice.exception;

import java.time.LocalDate;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.userservice.client.LoggingClient;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	private LoggingClient loggingClient;
	
	private final ObjectProvider<ErrorInfo> err;

    public GlobalExceptionHandler(ObjectProvider<ErrorInfo> err) {
            this.err = err;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> UserNotFoundExceptionHandler(UserNotFoundException ex){
            ErrorInfo errorInfo = err.getObject(); 
            errorInfo.setMessage(ex.getMessage());
            errorInfo.setDate(LocalDate.now());
            errorInfo.setStatus(HttpStatus.NOT_FOUND);
            loggingClient.logMessage("ERROR", ex.getMessage());
            return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
    
    
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorInfo> DuplicateEmailExceptionHandler(DuplicateEmailException ex){
            ErrorInfo errorInfo = err.getObject(); 
            errorInfo.setMessage(ex.getMessage());
            errorInfo.setDate(LocalDate.now());
            errorInfo.setStatus(HttpStatus.CONFLICT);
            loggingClient.logMessage("WARN", ex.getMessage());
            return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }
    
    
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorInfo> handleFeignException(FeignException ex) {
        ErrorInfo errorInfo = err.getObject();
        errorInfo.setDate(LocalDate.now());
        
        if(ex.status() == 404) {
            errorInfo.setMessage("Requested resource not found");
            errorInfo.setStatus(HttpStatus.NOT_FOUND);
            loggingClient.logMessage("ERROR", "Resource not found: " + ex.getMessage());
        } else {
            errorInfo.setMessage("Service communication error");
            errorInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            loggingClient.logMessage("ERROR", "Feign client error: " + ex.getMessage());
        }
        
        return new ResponseEntity<>(errorInfo, HttpStatus.valueOf(ex.status()));
    }
    
    @ExceptionHandler({CartEmptyException.class, ProductNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<ErrorInfo> handleServiceExceptions(RuntimeException ex) {
        ErrorInfo errorInfo = err.getObject();
        errorInfo.setMessage(ex.getMessage());
        errorInfo.setDate(LocalDate.now());
        errorInfo.setStatus(HttpStatus.BAD_REQUEST);
        loggingClient.logMessage("WARN", ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
    

    @ExceptionHandler(DeletionNotAllowedException.class)
    public ResponseEntity<ErrorInfo> Exceptions(DeletionNotAllowedException ex) {
        ErrorInfo errorInfo = err.getObject();
        errorInfo.setMessage(ex.getMessage());
        errorInfo.setDate(LocalDate.now());
        errorInfo.setStatus(HttpStatus.BAD_REQUEST);
        loggingClient.logMessage("WARN", ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
    
    
    
    

}
