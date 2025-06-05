package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import java.util.List;

public interface IProductService {
    ProductResponse createProduct(ProductRequest productRequest) throws Exception ;
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id) throws ProductNotFoundException;
    List<ProductResponse> getProductsByCategory(String category) throws ProductNotFoundException;
    List<ProductResponse> getProductsByName(String name) throws ProductNotFoundException;
    ProductResponse updateProduct(Long id, ProductRequest productRequest) throws ProductNotFoundException;
    String deleteProduct(Long id) throws ProductNotFoundException;
    ProductResponse decrementStock(Long id, Integer quantity) throws ProductNotFoundException;
    ProductResponse updateStock(Long id, Integer newStock) throws ProductNotFoundException;
}
