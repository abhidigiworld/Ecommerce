package com.cts.controller;

import org.springframework.security.core.Authentication; // ✅ Correct

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.client.UserClient;
import com.cts.dto.UserRequest;
import com.cts.entity.UserCredentials;
import com.cts.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserClient userClient;
	
	@PostMapping("/register")
	public String addNewUser(@RequestBody UserRequest userRequest)
	{
		UserCredentials userCredentials=UserCredentials.builder().email(userRequest.getEmail()).password(userRequest.getPassword()).build();
		userClient.createUser(userRequest);
		return authService.saveUser(userCredentials);
	}
	
	@PostMapping("/token")
	public String getToken(@RequestBody UserCredentials userCredentials) {
	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(userCredentials.getEmail(), userCredentials.getPassword())
	    );

	    if (authentication.isAuthenticated()) {
	        return authService.generateToken(userCredentials);
	    }
	    
	    throw new RuntimeException("Authentication failed");
	}
	
	

	
	@PostMapping("/validate")
	public String validateToken(@RequestParam("token") String token)
	{
		authService.validateToken(token);
		
		return "Token is valid";
	}
	
	@PostMapping("/verify/{email}")
	public String verifyUsercredentials(@PathVariable String email)
	{
		return authService.verifyUser(email);
	}
}
