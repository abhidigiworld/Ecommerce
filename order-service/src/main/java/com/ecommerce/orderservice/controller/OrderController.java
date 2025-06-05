package com.ecommerce.orderservice.controller;
 
import com.ecommerce.orderservice.client.CartClient;
import com.ecommerce.orderservice.client.LoggingClient;
import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.client.UserClient;
import com.ecommerce.orderservice.dto.*;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.entity.OrderStatus;

import com.ecommerce.orderservice.entity.PaymentStatus;
import com.ecommerce.orderservice.exception.CartEmptyException;
import com.ecommerce.orderservice.exception.InvalidOrderStatusException;
import com.ecommerce.orderservice.exception.OrderNotFoundException;
import com.ecommerce.orderservice.exception.UserNotFoundException;
import com.ecommerce.orderservice.repository.OrderItemRepository;

import com.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

 
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
 
@RestController
@RequestMapping("/api/orders")
public class OrderController {
 
    @Autowired
    private OrderRepository orderRepository;
 
    @Autowired
    private OrderItemRepository orderItemRepository;
 
    @Autowired
    private UserClient userClient;
 
    @Autowired
    private CartClient cartClient;
 
    @Autowired
    private ProductClient productClient;
    
    @Autowired
    private LoggingClient loggingClient;
 
    @PostMapping("/")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) throws UserNotFoundException, CartEmptyException {
        loggingClient.logMessage("INFO", "Placing order for user ID: " + orderRequest.getUserId());
        UserClientResponse userDetails = userClient.getUserById(orderRequest.getUserId());
        if (userDetails == null) {
            loggingClient.logMessage("ERROR", "User not found with ID: " + orderRequest.getUserId());
            throw new UserNotFoundException("User not found with ID: " + orderRequest.getUserId());
        }
        CartClientResponse cart = cartClient.getCartByUserId(orderRequest.getUserId());
        if (cart == null || cart.getItems().isEmpty()) {
            loggingClient.logMessage("ERROR", "Cart is empty for user ID: " + orderRequest.getUserId());
            throw new CartEmptyException("Cart is empty for user ID: " + orderRequest.getUserId());
        }

        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .shippingAddress(orderRequest.getShippingAddress() != null ? orderRequest.getShippingAddress() : userDetails.getShippingAddress())
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .totalPrice(cart.getCartTotal())
                .isDelete(false)
                .build();

        Order savedOrder = orderRepository.save(order);
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            productClient.decrementStock(cartItem.getProductId(), cartItem.getQuantity());
            return OrderItem.builder()
                    .order(savedOrder)
                    .productId(cartItem.getProductId())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getUnitPrice())
                    .subtotal(cartItem.getTotalPrice())
                    .build();
        }).collect(Collectors.toList());
        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);
        cartClient.clearCartByUserId(orderRequest.getUserId());
        savedOrder.setPaymentStatus(PaymentStatus.PAID);
        savedOrder.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(savedOrder);
        loggingClient.logMessage("INFO", "Order placed successfully with ID: " + savedOrder.getOrderId());
        return new ResponseEntity<>(convertToResponse(savedOrder, userDetails), HttpStatus.CREATED);
    }

 
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        loggingClient.logMessage("INFO", "Fetching order with ID: " + id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                	loggingClient.logMessage("ERROR", "Order not found with ID: " + id);
                    return new OrderNotFoundException("Order not found with ID: " + id);
                });

        UserClientResponse userDetails = userClient.getUserById(order.getUserId());
        return new ResponseEntity<>(convertToResponse(order, userDetails), HttpStatus.OK);
    }

    
    @GetMapping("/")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        loggingClient.logMessage("INFO", "Fetching all orders");
        List<Order> orders = orderRepository.findAll()
        		.stream()
        		.filter(order->!order.isDelete())
        		.collect(Collectors.toList());
        List<OrderResponse> responses = orders.stream()
            .map(order -> {
                UserClientResponse userDetails = userClient.getUserById(order.getUserId());
                return convertToResponse(order, userDetails);
            })
            .collect(Collectors.toList());
        loggingClient.logMessage("INFO", "Fetched " + responses.size() + " orders");
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
 
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) throws UserNotFoundException, OrderNotFoundException {
        loggingClient.logMessage("INFO", "Fetching orders for user ID: " + userId);
        UserClientResponse userDetails = userClient.getUserById(userId);
        if (userDetails == null) {
            loggingClient.logMessage("ERROR", "User not found with ID: " + userId);
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        List<Order> orders = orderRepository.findByUserId(userId)
        		.stream()
                .filter(order -> !order.isDelete())
                .collect(Collectors.toList());
        if(orders.isEmpty()) {
        	throw new OrderNotFoundException("Order not found with User ID: " +userId);
        }
        List<OrderResponse> orderResponses = orders.stream()
                .map(order -> convertToResponse(order, userDetails))
                .collect(Collectors.toList());
        loggingClient.logMessage("INFO", "Fetched " + orderResponses.size() + " orders for user ID: " + userId);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

 
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) throws OrderNotFoundException, InvalidOrderStatusException {
        loggingClient.logMessage("INFO", "Updating order status for order ID: " + id);
        Order existingOrder = orderRepository.findById(id)
        		.orElseThrow(() -> {
                    loggingClient.logMessage("ERROR", "Order not found with ID: " + id);
                    return new OrderNotFoundException("Order not found with ID: " + id);
                });

        if (request.getNewStatus() == null) {
            loggingClient.logMessage("ERROR", "Invalid order status update request: Status is null");
            throw new InvalidOrderStatusException("Order status cannot be null.");
        }

        existingOrder.setOrderStatus(request.getNewStatus());

        Order updatedOrder = orderRepository.save(existingOrder);
        UserClientResponse userDetails = userClient.getUserById(updatedOrder.getUserId());

        loggingClient.logMessage("INFO", "Order status updated successfully for order ID: " + id);
        return new ResponseEntity<>(convertToResponse(updatedOrder, userDetails), HttpStatus.OK);
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) throws OrderNotFoundException, InvalidOrderStatusException {
        loggingClient.logMessage("INFO", "Attempting to cancel order with ID: " + id);

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    loggingClient.logMessage("ERROR", "Order not found with ID: " + id);
                    return new OrderNotFoundException("Order not found with ID: " + id);
                });

        if (!existingOrder.getOrderStatus().canBeCancelled()) {
            loggingClient.logMessage("ERROR", "Order with ID " + id + " cannot be canceled in its current status: " + existingOrder.getOrderStatus());
            throw new InvalidOrderStatusException("Order cannot be canceled in its current status.");
        }

        existingOrder.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(existingOrder);

        loggingClient.logMessage("INFO", "Order successfully canceled with ID: " + id);
        return new ResponseEntity<>("Order with ID " + id + " has been successfully canceled.", HttpStatus.OK);
    }


    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long id) throws OrderNotFoundException {
        loggingClient.logMessage("INFO", "Attempting to delete order with ID: " + id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    loggingClient.logMessage("ERROR", "Order not found with ID: " + id);
                    return new OrderNotFoundException("Order not found with ID: " + id);
                });
        order.setDelete(true);
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        loggingClient.logMessage("INFO", "Order deleted successfully with ID: " + id);
        return new ResponseEntity<>("Order with ID " + id + " deleted successfully", HttpStatus.OK);
    }
    
 
    private OrderResponse convertToResponse(Order order, UserClientResponse userDetails) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> {
                    ProductClientResponse productDetails = productClient.getProductById(item.getProductId());
                    return OrderItemResponse.builder()
                            .orderItemId(item.getOrderItemId())
                            .productId(item.getProductId())
                            .productName(productDetails != null ? productDetails.getName() : "Unknown Product")
                            .productPrice(item.getUnitPrice())
                            .quantity(item.getQuantity())
                            .subtotal(item.getSubtotal())
                            .build();
                })
                .collect(Collectors.toList());
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .userName(userDetails != null ? userDetails.getName() : "Unknown User")
                .userEmail(userDetails != null ? userDetails.getEmail() : "unknown@example.com")
                .totalPrice(order.getTotalPrice())
                .shippingAddress(order.getShippingAddress())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .orderDate(order.getOrderDate())
                .orderItems(itemResponses)
                .build();

    }

}
 