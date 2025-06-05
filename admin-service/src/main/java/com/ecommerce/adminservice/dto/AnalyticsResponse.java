package com.ecommerce.adminservice.dto;
 
import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;
 
@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class AnalyticsResponse {

    private Long totalOrders;

    private Double totalSalesRevenue;

    private Long totalUsers;

    private Long totalProducts;

    // Add more fields as needed, e.g., average order value, top selling product names

}
 