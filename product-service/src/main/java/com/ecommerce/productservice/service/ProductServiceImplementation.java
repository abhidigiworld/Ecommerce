package com.ecommerce.productservice.service;

import com.ecommerce.productservice.client.LoggingClient;
import com.ecommerce.productservice.controller.ProductAlreadyExistsException;
import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LoggingClient loggingClient;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Optional<Product> existingProduct = productRepository.findByNameIgnoreCase(productRequest.getName());
        
        if (existingProduct.isPresent()) {
            String errorMessage = "Product already exists with name: " + productRequest.getName();
            loggingClient.logMessage("INFO", errorMessage);
            throw new ProductAlreadyExistsException(errorMessage);
        }
        
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .category(productRequest.getCategory())
                .imageUrl(productRequest.getImageUrl())
                .stockQuantity(productRequest.getStockQuantity())
                .build();
        
        Product savedProduct = productRepository.save(product);
        loggingClient.logMessage("INFO", "Product created: " + savedProduct.getProductId());
        return convertToResponse(savedProduct);
    }



    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        return convertToResponse(product);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) throws ProductNotFoundException {
        List<Product> products = productRepository.findByCategory(category);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found in category: " + category);
        }
        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByName(String name) throws ProductNotFoundException {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found with name containing: " + name);
        }
        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());

        Product updatedProduct = productRepository.save(product);
        loggingClient.logMessage("INFO", "Updating product with ID: " + id);
        return convertToResponse(updatedProduct);
    }

    @Override
    public String deleteProduct(Long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        product.setIsdelete(true);
        productRepository.save(product);
        loggingClient.logMessage("INFO", "Deleting product with ID: " + id);
        return "Product with ID " + id + " has been deleted successfully.";
    }

    @Override
    public ProductResponse decrementStock(Long id, Integer quantity) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        if (quantity < 0 || product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Invalid stock decrement quantity.");
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateStock(Long id, Integer newStock) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        product.setStockQuantity(product.getStockQuantity() + newStock);
        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}
