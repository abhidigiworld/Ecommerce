package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/api/products")
public class PublicProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.getProductsByCategory(category), HttpStatus.OK);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductResponse>> getProductsByName(@PathVariable String name) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.getProductsByName(name), HttpStatus.OK);
    }
}
