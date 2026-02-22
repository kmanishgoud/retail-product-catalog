package com.retail.product_catalog.service;

import com.retail.product_catalog.dto.ProductDTO;
import com.retail.product_catalog.exception.ResourceNotFoundException;
import com.retail.product_catalog.model.Product;
import com.retail.product_catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;
    private ProductDTO sampleProductDTO;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product(
                "iPhone 15",
                "Latest Apple smartphone",
                new BigDecimal("999.99"),
                "Electronics",
                10,
                "https://example.com/iphone.jpg"
        );
        sampleProduct.setId(1L);

        sampleProductDTO = new ProductDTO(
                1L,
                "iPhone 15",
                "Latest Apple smartphone",
                new BigDecimal("999.99"),
                "Electronics",
                10,
                "https://example.com/iphone.jpg"
        );
    }

    // ===================== GET ALL =====================

    @Test
    @DisplayName("Should return paginated list of products")
    void getAllProducts_ShouldReturnPagedProducts() {
        Page<Product> productPage = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        Page<ProductDTO> result = productService.getAllProducts(0, 5, "id");

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("iPhone 15");
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return empty page when no products exist")
    void getAllProducts_ShouldReturnEmptyPage_WhenNoProducts() {
        Page<Product> emptyPage = new PageImpl<>(List.of());
        when(productRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<ProductDTO> result = productService.getAllProducts(0, 5, "id");

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    // ===================== GET BY ID =====================

    @Test
    @DisplayName("Should return product when valid ID is provided")
    void getProductById_ShouldReturnProduct_WhenValidId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductDTO result = productService.getProductById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("iPhone 15");
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("999.99"));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product ID does not exist")
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, times(1)).findById(99L);
    }

    // ===================== CREATE =====================

    @Test
    @DisplayName("Should create and return new product")
    void createProduct_ShouldSaveAndReturnProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductDTO result = productService.createProduct(sampleProductDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("iPhone 15");
        assertThat(result.getCategory()).isEqualTo("Electronics");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should map all DTO fields correctly when creating product")
    void createProduct_ShouldMapAllFieldsCorrectly() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductDTO result = productService.createProduct(sampleProductDTO);

        assertThat(result.getName()).isEqualTo(sampleProductDTO.getName());
        assertThat(result.getDescription()).isEqualTo(sampleProductDTO.getDescription());
        assertThat(result.getPrice()).isEqualByComparingTo(sampleProductDTO.getPrice());
        assertThat(result.getCategory()).isEqualTo(sampleProductDTO.getCategory());
        assertThat(result.getStockQuantity()).isEqualTo(sampleProductDTO.getStockQuantity());
        assertThat(result.getImageUrl()).isEqualTo(sampleProductDTO.getImageUrl());
    }

    // ===================== UPDATE =====================

    @Test
    @DisplayName("Should update and return updated product")
    void updateProduct_ShouldUpdateAndReturnProduct() {
        ProductDTO updateDTO = new ProductDTO(
                1L,
                "iPhone 15 Pro",
                "Updated Apple smartphone with new features",
                new BigDecimal("1099.99"),
                "Electronics",
                5,
                "https://example.com/iphone15pro.jpg"
        );

        Product updatedProduct = new Product(
                "iPhone 15 Pro",
                "Updated Apple smartphone with new features",
                new BigDecimal("1099.99"),
                "Electronics",
                5,
                "https://example.com/iphone15pro.jpg"
        );
        updatedProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductDTO result = productService.updateProduct(1L, updateDTO);

        assertThat(result.getName()).isEqualTo("iPhone 15 Pro");
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("1099.99"));
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent product")
    void updateProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(99L, sampleProductDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).save(any(Product.class));
    }

    // ===================== DELETE =====================

    @Test
    @DisplayName("Should delete product when valid ID is provided")
    void deleteProduct_ShouldDeleteProduct_WhenValidId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).delete(any(Product.class));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(sampleProduct);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent product")
    void deleteProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).delete(any(Product.class));
    }

    // ===================== SEARCH =====================

    @Test
    @DisplayName("Should return matching products when searching by keyword")
    void searchProducts_ShouldReturnMatchingProducts() {
        Page<Product> productPage = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findByNameContainingIgnoreCase(
                eq("iphone"), any(Pageable.class))).thenReturn(productPage);

        Page<ProductDTO> result = productService.searchProducts("iphone", 0, 5, "id");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).containsIgnoringCase("iphone");
    }

    @Test
    @DisplayName("Should return empty page when search finds no matches")
    void searchProducts_ShouldReturnEmptyPage_WhenNoMatches() {
        Page<Product> emptyPage = new PageImpl<>(List.of());
        when(productRepository.findByNameContainingIgnoreCase(
                eq("xyz"), any(Pageable.class))).thenReturn(emptyPage);

        Page<ProductDTO> result = productService.searchProducts("xyz", 0, 5, "id");

        assertThat(result.getContent()).isEmpty();
    }

    // ===================== FILTER BY CATEGORY =====================

    @Test
    @DisplayName("Should return products filtered by category")
    void getProductsByCategory_ShouldReturnFilteredProducts() {
        Page<Product> productPage = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findByCategory(
                eq("Electronics"), any(Pageable.class))).thenReturn(productPage);

        Page<ProductDTO> result = productService.getProductsByCategory("Electronics", 0, 5, "id");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory()).isEqualTo("Electronics");
    }
}