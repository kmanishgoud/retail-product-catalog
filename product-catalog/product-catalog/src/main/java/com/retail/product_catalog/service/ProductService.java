package com.retail.product_catalog.service;

import com.retail.product_catalog.dto.ProductDTO;
import com.retail.product_catalog.exception.ResourceNotFoundException;
import com.retail.product_catalog.model.Product;
import com.retail.product_catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ===================== GET ALL (PAGINATED) =====================

    public Page<ProductDTO> getAllProducts(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.map(this::mapToDTO);
    }

    // ===================== GET BY ID =====================

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return mapToDTO(product);
    }

    // ===================== CREATE =====================

    public ProductDTO createProduct(ProductDTO dto) {
        Product product = mapToEntity(dto);
        Product saved = productRepository.save(product);
        return mapToDTO(saved);
    }

    // ===================== UPDATE =====================

    public ProductDTO updateProduct(Long id, ProductDTO dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setStockQuantity(dto.getStockQuantity());
        product.setImageUrl(dto.getImageUrl());

        Product updated = productRepository.save(product);

        return mapToDTO(updated);
    }

    // ===================== DELETE =====================

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }

    // ===================== SEARCH (PAGINATED) =====================

    public Page<ProductDTO> searchProducts(String searchTerm, int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Product> productPage =
                productRepository.findByNameContainingIgnoreCase(searchTerm, pageable);

        return productPage.map(this::mapToDTO);
    }

    // ===================== FILTER BY CATEGORY (PAGINATED) =====================

    public Page<ProductDTO> getProductsByCategory(String category, int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Product> productPage =
                productRepository.findByCategory(category, pageable);

        return productPage.map(this::mapToDTO);
    }

    // ===================== MAPPERS =====================

    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStockQuantity(),
                product.getImageUrl()
        );
    }

    private Product mapToEntity(ProductDTO dto) {
        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setStockQuantity(dto.getStockQuantity());
        product.setImageUrl(dto.getImageUrl());

        return product;
    }
}
