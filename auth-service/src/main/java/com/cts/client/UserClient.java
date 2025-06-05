package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.dto.UserRequest;
import com.cts.dto.UserResponse;

@FeignClient(name = "user-service")
public interface UserClient {
	
	 @PostMapping("/api/users")
	    UserResponse createUser(@RequestBody UserRequest userRequest);
	
}
