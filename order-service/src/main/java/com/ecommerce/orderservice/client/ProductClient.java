package com.ecommerce.orderservice.client;
 
import com.ecommerce.orderservice.dto.ProductClientResponse;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
 
@FeignClient(name = "product-service")

public interface ProductClient {

    @GetMapping("/api/products/{id}")

    ProductClientResponse getProductById(@PathVariable("id") Long id);
 
    // This endpoint should be added to ProductService's ProductController

    @PutMapping("/api/products/{id}/decrement-stock")

    ProductClientResponse decrementStock(@PathVariable("id") Long id, @RequestBody Integer quantity);

}
 