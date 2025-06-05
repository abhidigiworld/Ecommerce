package com.ecommerce.productservice.controller;
 
import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

 
import java.util.List;
 
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) throws Exception {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.updateProduct(id, productRequest), HttpStatus.OK);
    }
    
    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.getProductsByCategory(category), HttpStatus.OK);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductResponse>> getProductsByName(@PathVariable String name) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.getProductsByName(name), HttpStatus.OK);
    }

    @PutMapping("/{id}/decrement-stock")
    public ResponseEntity<ProductResponse> decrementStock(@PathVariable Long id, @RequestBody Integer quantity) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.decrementStock(id, quantity), HttpStatus.OK);
    }

    @PutMapping("/{id}/update-stock")
    public ResponseEntity<ProductResponse> updateStock(@PathVariable Long id, @RequestBody Integer newStock) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.updateStock(id, newStock), HttpStatus.OK);
    }

}
