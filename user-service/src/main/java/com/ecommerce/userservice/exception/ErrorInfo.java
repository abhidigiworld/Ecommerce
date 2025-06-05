package com.ecommerce.userservice.exception;

import java.time.LocalDate;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Scope("prototype")
public class ErrorInfo {
	private String message;
	private LocalDate date;
	private HttpStatus status;
}
