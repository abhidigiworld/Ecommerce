package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.client.LoggingClient;
import com.ecommerce.cartservice.client.ProductClient;
import com.ecommerce.cartservice.client.UserClient;
import com.ecommerce.cartservice.dto.CartItemRequest;
import com.ecommerce.cartservice.dto.CartItemResponse;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.ProductClientResponse;
import com.ecommerce.cartservice.dto.UserClientResponse;
import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.exception.CartItemNotFoundException;
import com.ecommerce.cartservice.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImplementation implements ICartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private LoggingClient loggingClient;

    @Override
    public CartItemResponse addOrUpdateCartItem(CartItemRequest cartItemRequest) throws CartItemNotFoundException {
        loggingClient.logMessage("INFO", "Received request to add/update cart item: " + cartItemRequest);

        UserClientResponse userDetails = userClient.getUserById(cartItemRequest.getUserId());
        if (userDetails == null) {
            loggingClient.logMessage("ERROR", "User not found with ID: " + cartItemRequest.getUserId());
            throw new IllegalArgumentException("User not found with ID: " + cartItemRequest.getUserId());
        }

        ProductClientResponse productDetails = productClient.getProductById(cartItemRequest.getProductId());
        if (productDetails == null) {
            loggingClient.logMessage("ERROR", "Product not found with ID: " + cartItemRequest.getProductId());
            throw new CartItemNotFoundException("Product not found with ID: " + cartItemRequest.getProductId());
        }

        loggingClient.logMessage("INFO", "Processing cart item for product: " + productDetails.getName());

        int requestedQty = cartItemRequest.getQuantity() != null ? cartItemRequest.getQuantity() : 1;
        if (requestedQty<0) {
            loggingClient.logMessage("ERROR", "Negative value is not allowed");
            throw new IllegalArgumentException("Negative value is not allowed");
        }
        if (productDetails.getStockQuantity() <= requestedQty) {
            loggingClient.logMessage("ERROR", "Insufficient stock for product: " + productDetails.getName());
            throw new IllegalArgumentException("Insufficient stock for product: " + productDetails.getName());
        }

        Optional<CartItem> existingOpt = cartItemRepository.findByUserIdAndProductId(cartItemRequest.getUserId(), cartItemRequest.getProductId());

        CartItem cartItem = existingOpt.orElseGet(() -> CartItem.builder()
                .userId(cartItemRequest.getUserId())
                .productId(cartItemRequest.getProductId())
                .quantity(requestedQty)
                .build()
        );

        if (existingOpt.isPresent()) {
            if (productDetails.getStockQuantity() < (cartItem.getQuantity() + requestedQty)) {
                loggingClient.logMessage("ERROR", "Insufficient stock for product: " + productDetails.getName());
                throw new IllegalArgumentException("Insufficient stock for product: " + productDetails.getName());
            }
            cartItem.setQuantity(cartItem.getQuantity() + requestedQty);
        }

        cartItem.setUnitPrice(productDetails.getPrice());
        cartItem.setTotalPrice(cartItem.getUnitPrice() * cartItem.getQuantity());

        CartItem saved = cartItemRepository.save(cartItem);
        loggingClient.logMessage("INFO", "Cart item added/updated successfully: " + saved);
        return convertToResponse(saved, productDetails);
    }

    @Override
    public void removeCartItem(Long cartItemId) throws CartItemNotFoundException {
        loggingClient.logMessage("INFO", "Request received to remove cart item ID: " + cartItemId);

        if (!cartItemRepository.existsById(cartItemId)) {
            loggingClient.logMessage("ERROR", "Cart item not found with ID: " + cartItemId);
            throw new CartItemNotFoundException("Cart item not found with ID: " + cartItemId);
        }

        cartItemRepository.deleteById(cartItemId);
        loggingClient.logMessage("INFO", "Cart item removed successfully with ID: " + cartItemId);
    }

    @Override
    public CartResponse getCartByUserId(Long userId) throws CartItemNotFoundException {
        loggingClient.logMessage("INFO", "Fetching cart for user ID: " + userId);

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            loggingClient.logMessage("ERROR", "Cart not found for user ID: " + userId);
            throw new CartItemNotFoundException("Cart not found for user ID: " + userId);
        }

        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(item -> {
                    ProductClientResponse productDetails = productClient.getProductById(item.getProductId());
                    return convertToResponse(item, productDetails);
                })
                .collect(Collectors.toList());

        Double cartTotal = itemResponses.stream().mapToDouble(CartItemResponse::getTotalPrice).sum();
        CartResponse cartResponse = CartResponse.builder()
                .userId(userId)
                .items(itemResponses)
                .cartTotal(cartTotal)
                .build();

        loggingClient.logMessage("INFO", "Cart fetched successfully for user ID: " + userId);
        return cartResponse;
    }

    @Override
    @Transactional
    public String clearCartByUserId(Long userId) throws CartItemNotFoundException {
        loggingClient.logMessage("INFO", "Received request to clear cart for user ID: " + userId);

        if (cartItemRepository.findByUserId(userId).isEmpty()) {
            loggingClient.logMessage("ERROR", "No cart items found for user ID: " + userId);
            throw new CartItemNotFoundException("No cart items found for user ID: " + userId);
        }

        cartItemRepository.deleteByUserId(userId);
        loggingClient.logMessage("INFO", "Cart cleared successfully for user ID: " + userId);
        return "Cart cleared successfully for user ID: " + userId;
    }


    private CartItemResponse convertToResponse(CartItem cartItem, ProductClientResponse productDetails) {
        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .userId(cartItem.getUserId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(cartItem.getTotalPrice())
                .productName(productDetails != null ? productDetails.getName() : "Unknown Product")
                .productPrice(productDetails != null ? productDetails.getPrice() : 0.0)
                .imageUrl(productDetails != null ? productDetails.getImageUrl() : null)
                .build();
    }
}
