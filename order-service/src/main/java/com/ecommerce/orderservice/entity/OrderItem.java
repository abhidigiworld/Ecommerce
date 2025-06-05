package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;
 
@Entity

@Table(name = "order_items")

@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class OrderItem {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long orderItemId;
 
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "order_id", nullable = false)

    private Order order;
 
    private Long productId;

    private Integer quantity;

    private Double unitPrice;

    private Double subtotal;

}
 
