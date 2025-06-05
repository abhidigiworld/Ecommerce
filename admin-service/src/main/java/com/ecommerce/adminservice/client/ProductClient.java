package com.ecommerce.adminservice.client;
 
import com.ecommerce.adminservice.dto.ProductClientResponse;

import com.ecommerce.adminservice.dto.ProductRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
 
@FeignClient(name = "product-service")

public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductClientResponse getProductById(@PathVariable Long id);
 
    @GetMapping("/api/products/")
    List<ProductClientResponse> getAllProducts();
    
    @GetMapping("/api/products/category/{category}")
    ResponseEntity<List<ProductClientResponse>> getProductsByCategory(@PathVariable String category);

    @GetMapping("/api/products/search/{name}")
    ResponseEntity<List<ProductClientResponse>> searchProductsByName(@PathVariable String name);
 
    @PostMapping("/api/products/")
    ProductClientResponse createProduct(@RequestBody ProductRequest productRequest);
 
    @PutMapping("/api/products/{id}")
    ProductClientResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest);
 
    @DeleteMapping("/api/products/{id}")
    void deleteProduct(@PathVariable Long id);
    
    @PutMapping("/api/products/{id}/decrement-stock")
    public ProductClientResponse decrementStock(@PathVariable Long id, @RequestBody Integer quantity);
    
    @PutMapping("/api/products/{id}/update-stock")
    public ProductClientResponse updateStock(@PathVariable Long id, @RequestBody Integer newStock);

}
 