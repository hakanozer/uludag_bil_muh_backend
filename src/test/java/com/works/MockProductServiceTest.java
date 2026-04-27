package com.works;

import com.works.dto.ProductSaveRequestDto;
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

import java.math.BigDecimal;
import java.util.Arrays;

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


}
