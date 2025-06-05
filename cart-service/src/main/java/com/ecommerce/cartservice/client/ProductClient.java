package com.ecommerce.cartservice.client;

import com.ecommerce.cartservice.dto.ProductClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
@FeignClient(name = "product-service")
public interface ProductClient {
 
    @GetMapping("/api/products/{id}")
    ProductClientResponse getProductById(@PathVariable("id") Long id);
}
