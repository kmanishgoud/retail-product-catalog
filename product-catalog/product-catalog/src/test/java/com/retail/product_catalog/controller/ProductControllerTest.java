package com.retail.product_catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.product_catalog.dto.ProductDTO;
import com.retail.product_catalog.exception.ResourceNotFoundException;
import com.retail.product_catalog.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController Unit Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO sampleProductDTO;

    @BeforeEach
    void setUp() {
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
    @DisplayName("GET /api/products - Should return 200 with paginated products")
    void getAllProducts_ShouldReturn200() throws Exception {
        Page<ProductDTO> page = new PageImpl<>(List.of(sampleProductDTO));
        when(productService.getAllProducts(anyInt(), anyInt(), anyString())).thenReturn(page);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("iPhone 15"))
                .andExpect(jsonPath("$.content[0].price").value(999.99))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    // ===================== GET BY ID =====================

    @Test
    @DisplayName("GET /api/products/{id} - Should return 200 with product")
    void getProductById_ShouldReturn200_WhenProductExists() throws Exception {
        when(productService.getProductById(1L)).thenReturn(sampleProductDTO);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.category").value("Electronics"));
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return 404 when product not found")
    void getProductById_ShouldReturn404_WhenProductNotFound() throws Exception {
        when(productService.getProductById(99L))
                .thenThrow(new ResourceNotFoundException("Product not found with id: 99"));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    // ===================== CREATE =====================

    @Test
    @DisplayName("POST /api/products - Should return 200 with created product")
    void createProduct_ShouldReturn200_WhenValidRequest() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(sampleProductDTO);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProductDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    @DisplayName("POST /api/products - Should return 400 when request is invalid")
    void createProduct_ShouldReturn400_WhenInvalidRequest() throws Exception {
        ProductDTO invalidDTO = new ProductDTO(
                null, "A", "short", new BigDecimal("-1"),
                "", -1, ""
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    // ===================== UPDATE =====================

    @Test
    @DisplayName("PUT /api/products/{id} - Should return 200 with updated product")
    void updateProduct_ShouldReturn200_WhenValidRequest() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductDTO.class)))
                .thenReturn(sampleProductDTO);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProductDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15"));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Should return 404 when product not found")
    void updateProduct_ShouldReturn404_WhenProductNotFound() throws Exception {
        when(productService.updateProduct(eq(99L), any(ProductDTO.class)))
                .thenThrow(new ResourceNotFoundException("Product not found with id: 99"));

        mockMvc.perform(put("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProductDTO)))
                .andExpect(status().isNotFound());
    }

    // ===================== DELETE =====================

    @Test
    @DisplayName("DELETE /api/products/{id} - Should return 204 when product deleted")
    void deleteProduct_ShouldReturn204_WhenProductExists() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Should return 404 when product not found")
    void deleteProduct_ShouldReturn404_WhenProductNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Product not found with id: 99"))
                .when(productService).deleteProduct(99L);

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    // ===================== SEARCH =====================

    @Test
    @DisplayName("GET /api/products/search - Should return 200 with matching products")
    void searchProducts_ShouldReturn200() throws Exception {
        Page<ProductDTO> page = new PageImpl<>(List.of(sampleProductDTO));
        when(productService.searchProducts(anyString(), anyInt(), anyInt(), anyString()))
                .thenReturn(page);

        mockMvc.perform(get("/api/products/search?keyword=iphone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("iPhone 15"));
    }
}