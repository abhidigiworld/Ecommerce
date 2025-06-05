package com.ecommerce.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.userservice.client.CartClient;
import com.ecommerce.userservice.client.LoggingClient;
import com.ecommerce.userservice.client.OrderClient;
import com.ecommerce.userservice.client.ProductClient;
import com.ecommerce.userservice.dto.CartItemRequest;
import com.ecommerce.userservice.dto.CartItemResponse;
import com.ecommerce.userservice.dto.CartResponse;
import com.ecommerce.userservice.dto.OrderRequest;
import com.ecommerce.userservice.dto.OrderResponse;
import com.ecommerce.userservice.dto.ProductClientResponse;
import com.ecommerce.userservice.exception.CartEmptyException;
import com.ecommerce.userservice.exception.OrderNotFoundException;
import com.ecommerce.userservice.exception.ProductNotFoundException;
import com.ecommerce.userservice.exception.UserNotFoundException;

import feign.FeignException;

@RestController
@RequestMapping("/api/users")
public class UserAggregationController {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private LoggingClient loggingClient;

    // Product all call for user
    @GetMapping("/products")
    public ResponseEntity<List<ProductClientResponse>> getAllProducts() {
        loggingClient.logMessage("INFO", "User fetching all products");
        try {
            return productClient.getAllProducts();
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException("Products not found");
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductClientResponse> getProductById(@PathVariable Long id) {
        loggingClient.logMessage("INFO", "User fetching product ID: " + id);
        try {
            return productClient.getProductById(id);
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    @GetMapping("/products/category/{category}")
    public ResponseEntity<List<ProductClientResponse>> getProductsByCategory(@PathVariable String category) {
        loggingClient.logMessage("INFO", "User browsing category: " + category);
        try {
            return productClient.getProductsByCategory(category);
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException("No products found in category: " + category);
        }
    }

    @GetMapping("/products/search/{name}")
    public ResponseEntity<List<ProductClientResponse>> searchProducts(@PathVariable String name) {
        loggingClient.logMessage("INFO", "User searching for: " + name);
        try {
            return productClient.searchProductsByName(name);
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException("No products found with name: " + name);
        }
    }

    @PostMapping("/cart/items")
    public ResponseEntity<CartItemResponse> addToCart(@RequestBody CartItemRequest request) throws UserNotFoundException {
        loggingClient.logMessage("INFO", "User adding to cart: " + request.getUserId());
        try {
            return cartClient.addOrUpdateCartItem(request);
        } catch (FeignException.NotFound ex) {
            throw new UserNotFoundException("User not found: " + request.getUserId());
        }
    }

    @DeleteMapping("/cart/items/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId) {
        loggingClient.logMessage("INFO", "User removing cart item: " + cartItemId);
        try {
            return cartClient.removeCartItem(cartItemId);
        } catch (FeignException.NotFound ex) {
            throw new CartEmptyException("Cart item not found: " + cartItemId);
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<CartResponse> getUserCart(@RequestParam Long userId) {
        loggingClient.logMessage("INFO", "User viewing cart: " + userId);
        try {
            return cartClient.getCartByUserId(userId);
        } catch (FeignException.NotFound ex) {
            throw new CartEmptyException("Cart is empty for user ID: " + userId);
        }
    }

    @DeleteMapping("/cart")
    public ResponseEntity<String> clearCart(@RequestParam Long userId) {
        loggingClient.logMessage("INFO", "User clearing cart: " + userId);
        try {
            return cartClient.clearCartByUserId(userId);
        } catch (FeignException.NotFound ex) {
            throw new CartEmptyException("Cannot clear cart. Cart not found for user ID: " + userId);
        }
    }

    // Order call for the user
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        loggingClient.logMessage("INFO", "User placing order: " + request.getUserId());
        try {
            return orderClient.placeOrder(request);
        } catch (FeignException.NotFound ex) {
            throw new OrderNotFoundException("Failed to place order for user ID: " + request.getUserId());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@RequestParam Long userId) {
        loggingClient.logMessage("INFO", "User viewing order history: " + userId);
        try {
            return orderClient.getOrdersByUserId(userId);
        } catch (FeignException.NotFound ex) {
            throw new OrderNotFoundException("No orders found for user ID: " + userId);
        }
    }
    
    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        loggingClient.logMessage("INFO", "User attempting to cancel order with ID: " + id);
        try {
            return orderClient.cancelOrder(id);
        } catch (FeignException.NotFound ex) {
            throw new OrderNotFoundException("Order not found for ID: " + id);
        } catch (FeignException.BadRequest ex) {
            throw new OrderNotFoundException("Order cannot be canceled in its current status.");
        }
    }

    
}
