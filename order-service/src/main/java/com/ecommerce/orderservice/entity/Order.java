package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
 
    private Long userId;
    private Double totalPrice;
    private String shippingAddress;
 
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
 
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
 
    private LocalDateTime orderDate;
    
    @Builder.Default
    private boolean isDelete=false;
 
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
    
}