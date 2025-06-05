package com.ecommerce.orderservice.client;
 
import com.ecommerce.orderservice.dto.UserClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserClientResponse getUserById(@PathVariable("id") Long id);
}