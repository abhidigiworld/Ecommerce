package com.ecommerce.adminservice.client;
 
import com.ecommerce.adminservice.dto.UserClientResponse;
import com.ecommerce.adminservice.dto.UserResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
 
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserClientResponse getUserById(@PathVariable("id") Long id);
 
    @GetMapping("/api/users/")
    List<UserClientResponse> getAllUsers();
    
    @DeleteMapping("/api/users/{id}/hard-delete")
	UserResponse hardDeleteUser(@PathVariable Long id);
    
    
}