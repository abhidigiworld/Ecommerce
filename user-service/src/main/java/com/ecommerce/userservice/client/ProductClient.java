package com.ecommerce.userservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.userservice.dto.ProductClientResponse;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/products/")
    ResponseEntity<List<ProductClientResponse>> getAllProducts();

    @GetMapping("/api/products/{id}")
    ResponseEntity<ProductClientResponse> getProductById(@PathVariable Long id);

    @GetMapping("/api/products/category/{category}")
    ResponseEntity<List<ProductClientResponse>> getProductsByCategory(@PathVariable String category);

    @GetMapping("/api/products/search/{name}")
    ResponseEntity<List<ProductClientResponse>> searchProductsByName(@PathVariable String name);
}