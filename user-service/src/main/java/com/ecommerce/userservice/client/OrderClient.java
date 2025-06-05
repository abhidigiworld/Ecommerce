package com.ecommerce.userservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecommerce.userservice.dto.OrderRequest;
import com.ecommerce.userservice.dto.OrderResponse;

@FeignClient(name = "order-service")
public interface OrderClient {
    @PostMapping("/api/orders/")
    ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request);

    @GetMapping("/api/orders/{id}")
    ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id);

    @GetMapping("/api/orders/user/{userId}")
    ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId);
    
    @PostMapping("/api/orders/{id}/cancel")
    ResponseEntity<String> cancelOrder(@PathVariable Long id);
}