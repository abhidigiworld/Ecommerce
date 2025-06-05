package com.ecommerce.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Table(name = "products") // Explicit table name
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; 
    private String name;
    private String description;
    private Double price;
    private String category;
    private String imageUrl;
    private Integer stockQuantity; 
    @Builder.Default
    private boolean isdelete=false;
    
}