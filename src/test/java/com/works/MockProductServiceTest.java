package com.works;

import com.works.dto.ProductSaveRequestDto;
import com.works.dto.ProductUpdateRequestDto;
import com.works.entity.Product;
import com.works.repository.ProductRepository;
import com.works.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ModelMapper model;

    @InjectMocks
    ProductService productService;

    @Test
    void saveProduct_success() {
        // Given
        // (Set up any necessary data or mock behavior here)
        ProductSaveRequestDto requestDto = new ProductSaveRequestDto();
        requestDto.setPrice(new BigDecimal("10.00"));
        requestDto.setTitle("Test Product");
        requestDto.setDescription("Test Description");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setTitle("Test");
        savedProduct.setDescription("Test Description");
        savedProduct.setPrice(new BigDecimal("10.00"));

        when(model.map(any(ProductSaveRequestDto.class), eq(Product.class))).thenReturn(savedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        // (Call the method you want to test here)
        Product result = productService.save(requestDto);

        // Then
        // (Assert the expected results here)
        assert result.getId() == 1L;
        assert result.getTitle().equals("Test");
        assert result.getDescription().equals("Test Description");
        assert result.getPrice().equals(new BigDecimal("10.00"));

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void saveProduct_throwsException_whenRepositoryFails() {
        // Given
        ProductSaveRequestDto requestDto = new ProductSaveRequestDto();
        requestDto.setPrice(new BigDecimal("10.00"));
        requestDto.setTitle("Failing Product");
        requestDto.setDescription("Failing Description");

        when(model.map(any(ProductSaveRequestDto.class), eq(Product.class)))
                .thenThrow(new RuntimeException("ModelMapper error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> productService.save(requestDto));

        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void listProduct_success() {
        // Given
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Test Product 1");
        product1.setDescription("Test Description 1");
        product1.setPrice(new BigDecimal("10.00"));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Test Product 2");
        product2.setDescription("Test Description 2");
        product2.setPrice(new BigDecimal("20.00"));

        Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2));

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        // When
        Page<Product> result = productService.productList(0);

        // Then
        assert result.getContent().size() == 2;
        assert result.getContent().get(0).getId() == 1L;
        assert result.getContent().get(0).getTitle().equals("Test Product 1");
        assert result.getContent().get(1).getId() == 2L;
        assert result.getContent().get(1).getTitle().equals("Test Product 2");

        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }


    @Test
    void updateProduct_success() {
        // Given
        ProductUpdateRequestDto updateRequestDto = new ProductUpdateRequestDto();
        updateRequestDto.setId(1L);
        updateRequestDto.setTitle("Updated Product");
        updateRequestDto.setDescription("Updated Description");
        updateRequestDto.setPrice(new BigDecimal("15.00"));

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setTitle("Original Product");
        existingProduct.setDescription("Original Description");
        existingProduct.setPrice(new BigDecimal("10.00"));

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setTitle("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(new BigDecimal("15.00"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(model.map(updateRequestDto, Product.class)).thenReturn(updatedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        ResponseEntity result = productService.update(updateRequestDto);

        // Then
        assert result.getStatusCode().value() == 200;
        assert ((Map<String, Object>) result.getBody()).get("success").equals(true);
        assert ((Map<String, Object>) result.getBody()).get("message").equals("Product updated successfully.");

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_throwsException_whenNotFound() {
        // Given
        ProductUpdateRequestDto updateRequestDto = new ProductUpdateRequestDto();
        updateRequestDto.setId(99L);
        updateRequestDto.setTitle("Non-existing Product");
        updateRequestDto.setDescription("Some Description");
        updateRequestDto.setPrice(new BigDecimal("50.00"));

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        ResponseEntity result = productService.update(updateRequestDto);

        // Then
        assert result.getStatusCode().value() == 404;
        assert ((Map<String, Object>) result.getBody()).get("success").equals(false);
        assert ((Map<String, Object>) result.getBody()).get("message").equals("Product not found id: 99");

        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void searchProduct_success() {
        // Given
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setDescription("Description 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Another Product");
        product2.setDescription("Another Description");

        String query = "Product";
        String sortBy = "asc";
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2));

        when(productRepository.findByTitleContainsOrDescriptionContainsAllIgnoreCase(eq(query), eq(query), any(Pageable.class)))
                .thenReturn(productPage);

        // When
        Page<Product> result = productService.search(query, 0, sortBy);

        // Then
        assert result.getTotalElements() == 2;
        assert result.getContent().get(0).getTitle().equals("Product 1");
        assert result.getContent().get(1).getTitle().equals("Another Product");

        verify(productRepository, times(1))
                .findByTitleContainsOrDescriptionContainsAllIgnoreCase(eq(query), eq(query), any(Pageable.class));
    }

    @Test
    void searchProduct_noResults() {
        // Given
        String query = "Non-existing";
        String sortBy = "desc";

        Page<Product> emptyPage = new PageImpl<>(Arrays.asList());

        when(productRepository.findByTitleContainsOrDescriptionContainsAllIgnoreCase(eq(query), eq(query), any(Pageable.class)))
                .thenReturn(emptyPage);

        // When
        Page<Product> result = productService.search(query, 0, sortBy);

        // Then
        assert result.isEmpty();

        verify(productRepository, times(1))
                .findByTitleContainsOrDescriptionContainsAllIgnoreCase(eq(query), eq(query), any(Pageable.class));
    }
}
