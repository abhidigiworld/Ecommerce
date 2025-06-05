package com.ecommerce.adminservice.dto;
 
import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;
 
@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class UpdateOrderStatusRequest {

    private String newStatus; // Changed to String to match the OrderClientResponse

}
 