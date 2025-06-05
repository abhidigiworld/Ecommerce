package com.ecommerce.adminservice.controller;

import com.ecommerce.adminservice.client.LoggingClient;
import com.ecommerce.adminservice.client.OrderClient;
import com.ecommerce.adminservice.client.ProductClient;
import com.ecommerce.adminservice.client.UserClient;
import com.ecommerce.adminservice.dto.*;
import com.ecommerce.adminservice.exception.OrderNotFoundException;
import com.ecommerce.adminservice.exception.ProductNotFoundException;
import com.ecommerce.adminservice.exception.UserNotFoundException;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderClient orderClient;
    
    @Autowired
    private LoggingClient loggingClient;

    // --- User Management ---

    @GetMapping("/users")
    public ResponseEntity<List<UserClientResponse>> getAllUsers() {
        loggingClient.logMessage("INFO", "Getting all the users");
        return ResponseEntity.ok(userClient.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserClientResponse> getUserById(@PathVariable Long id) throws UserNotFoundException {
    	loggingClient.logMessage("INFO", "Fetching user with ID: " + id);
        UserClientResponse user = userClient.getUserById(id);
        if (user == null) {
        	loggingClient.logMessage("ERROR", "User not found with ID: " + id);
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{id}/delete")
    public ResponseEntity<UserResponse> deleteUserId(@PathVariable Long id) throws UserNotFoundException{
    	loggingClient.logMessage("INFO", "Deleting user with ID: " + id);
    	UserClientResponse user = userClient.getUserById(id);
    	if (user == null) {
        	loggingClient.logMessage("ERROR", "User not found with ID: " + id);
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    	UserResponse userResponse=userClient.hardDeleteUser(id);
    	return new ResponseEntity<UserResponse>(userResponse,HttpStatus.OK);
    }

    // --- Product Management ---

    @PostMapping("/products")
    public ResponseEntity<ProductClientResponse> createProduct(@RequestBody ProductRequest productRequest) {
        loggingClient.logMessage("INFO", "Creating new product: " + productRequest.getName());
        ProductClientResponse newProduct = productClient.createProduct(productRequest);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductClientResponse>> getAllProducts() {
        loggingClient.logMessage("INFO", "Fetching all products");
        return ResponseEntity.ok(productClient.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductClientResponse> getProductById(@PathVariable Long id) throws ProductNotFoundException {
        ProductClientResponse product = productClient.getProductById(id);
        loggingClient.logMessage("INFO", "Fetching product with ID: " + id);
        if (product == null) {
            loggingClient.logMessage("ERROR", "Product not found with ID: " + id);
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        return ResponseEntity.ok(product);
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<ProductClientResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) throws ProductNotFoundException {
        loggingClient.logMessage("INFO", "Updating product with ID: " + id);
        ProductClientResponse updatedProduct = productClient.updateProduct(id, productRequest);
        if (updatedProduct == null) {
            loggingClient.logMessage("ERROR", "Product not found with ID: " + id);
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        loggingClient.logMessage("INFO", "Product updated successfully with ID: " + id);
        return ResponseEntity.ok(updatedProduct);
    }


    @PutMapping("/{id}/decrement-stock")
    public ResponseEntity<ProductClientResponse> decrementstock(@PathVariable Long id, @RequestBody Integer quantity) {
        ProductClientResponse response = productClient.decrementStock(id, quantity);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/update-stock")
    public ResponseEntity<ProductClientResponse> updatestock(@PathVariable Long id,@RequestBody Integer quantity){
    	ProductClientResponse response=productClient.updateStock(id, quantity);
    	return ResponseEntity.ok(response);
    }


    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ProductNotFoundException {
        loggingClient.logMessage("INFO", "Deleting product with ID: " + id);
        try {
            productClient.deleteProduct(id);
            loggingClient.logMessage("INFO", "Product deleted successfully with ID: " + id);
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound e) {
            loggingClient.logMessage("ERROR", "Product not found with ID: " + id);
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }
    
    @GetMapping("/products/category/{category}")
    public ResponseEntity<List<ProductClientResponse>> getProductsByCategory(@PathVariable String category) throws ProductNotFoundException {
        loggingClient.logMessage("INFO", "User browsing category: " + category);
        try {
            return productClient.getProductsByCategory(category);
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException("No products found in category: " + category);
        }
    }

    @GetMapping("/products/search/{name}")
    public ResponseEntity<List<ProductClientResponse>> searchProducts(@PathVariable String name) throws ProductNotFoundException {
        loggingClient.logMessage("INFO", "User searching for: " + name);
        try {
            return productClient.searchProductsByName(name);
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException("No products found with name: " + name);
        }
    }

    
    

    // --- Order Management ---

    @GetMapping("/orders")
    public ResponseEntity<List<OrderClientResponse>> getAllOrders() {
        loggingClient.logMessage("INFO", "Fetching all orders");
        return ResponseEntity.ok(orderClient.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderClientResponse> getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        loggingClient.logMessage("INFO", "Fetching order with ID: " + id);
        OrderClientResponse order = orderClient.getOrderById(id);
        if (order == null) {
            loggingClient.logMessage("ERROR", "Order not found with ID: " + id);
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        return ResponseEntity.ok(order);
    }


    @PutMapping("/orders/{id}/status")
    public ResponseEntity<OrderClientResponse> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) throws OrderNotFoundException {
        loggingClient.logMessage("INFO", "Updating order status for order ID: " + id);
        OrderClientResponse updatedOrder = orderClient.updateOrderStatus(id, request);
        if (updatedOrder == null) {
            loggingClient.logMessage("ERROR", "Order not found with ID: " + id);
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        loggingClient.logMessage("INFO", "Order status updated successfully for order ID: " + id);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) throws OrderNotFoundException {
        loggingClient.logMessage("INFO", "Attempting to delete order with ID: " + id);

        try {
            String response = orderClient.deleteOrder(id);
            loggingClient.logMessage("INFO", "Order deleted successfully with ID: " + id);
            return new ResponseEntity<String>(response,HttpStatus.OK);
        } catch (FeignException.NotFound e) {
            loggingClient.logMessage("ERROR", "Order not found with ID: " + id);
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
    }


    // --- Sales Analytics and Reports ---

    @GetMapping("/analytics/summary")
    public ResponseEntity<AnalyticsResponse> getSalesSummary() {
        loggingClient.logMessage("INFO", "Fetching sales summary");
        List<OrderClientResponse> allOrders = orderClient.getAllOrders();
        List<UserClientResponse> allUsers = userClient.getAllUsers();
        List<ProductClientResponse> allProducts = productClient.getAllProducts();

        long totalOrders = allOrders.size();
        double totalSalesRevenue = allOrders.stream()
                .mapToDouble(OrderClientResponse::getTotalPrice)
                .sum();
        long totalUsers = allUsers.size();
        long totalProducts = allProducts.size();

        AnalyticsResponse analytics = AnalyticsResponse.builder()
                .totalOrders(totalOrders)
                .totalSalesRevenue(totalSalesRevenue)
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .build();
        loggingClient.logMessage("INFO", "Sales summary retrieved successfully");
        return ResponseEntity.ok(analytics);
    }
}