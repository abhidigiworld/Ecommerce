package com.ecommerce.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.cartservice.dto.UserClientResponse;


@FeignClient(name = "user-service")
public interface UserClient {
	
	@GetMapping("/api/users/{id}")
	public UserClientResponse getUserById(@PathVariable Long id);
	
}
