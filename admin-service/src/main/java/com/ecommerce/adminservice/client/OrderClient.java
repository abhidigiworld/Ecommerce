package com.ecommerce.adminservice.client;
 
import com.ecommerce.adminservice.dto.OrderClientResponse;

import com.ecommerce.adminservice.dto.UpdateOrderStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
 
@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/orders/{id}")
    OrderClientResponse getOrderById(@PathVariable Long id);
 
    @GetMapping("/api/orders/user/{userId}")
    List<OrderClientResponse> getOrdersByUserId(@PathVariable Long userId);
 
    
    @GetMapping("/api/orders/")
    List<OrderClientResponse> getAllOrders();
    
    @DeleteMapping("/api/orders/{id}")
    String deleteOrder(@PathVariable Long id);
 
    @PutMapping("/api/orders/{id}/status")
    OrderClientResponse updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request);

}
 