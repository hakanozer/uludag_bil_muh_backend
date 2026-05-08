package com.works.service;

import com.works.dto.ProductSaveRequestDto;
import com.works.dto.ProductUpdateRequestDto;
import com.works.entity.Product;
import com.works.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    final ProductRepository productRepository;
    final ModelMapper model;

    @CacheEvict(cacheNames = "productListCache", allEntries = true)
    public Product save(ProductSaveRequestDto productSaveRequestDto) {
        Product product = model.map(productSaveRequestDto, Product.class);
        return productRepository.save(product);
    }

    @CacheEvict(cacheNames = "productListCache", allEntries = true)
    public List<Product> saveAll(List<ProductSaveRequestDto> productSaveRequestDtos){
        List<Product> productList = productSaveRequestDtos.stream()
                .map(dto -> model.map(dto, Product.class))
                .toList();
        return productRepository.saveAll(productList);
    }


    @CacheEvict(cacheNames = "productListCache", allEntries = true)
    public ResponseEntity deleteOne(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            productRepository.deleteById(id);
            Map<String, Object> hm = Map.of("success", true, "message", "Product deleted successfully.");
            return ResponseEntity.ok().body(hm);
        }else {
            Map<String, Object> hm = Map.of("success", false, "message", "Product not found id: " + id + "");
            return ResponseEntity.status(404).body(hm);
        }
    }

    @CacheEvict(cacheNames = "productListCache", allEntries = true)
    public ResponseEntity update(ProductUpdateRequestDto productUpdateRequestDto) {
        Optional<Product> optionalProduct = productRepository.findById(productUpdateRequestDto.getId());
        if(optionalProduct.isPresent()){
            Product product = model.map(productUpdateRequestDto, Product.class);
            productRepository.save(product);
            Map<String, Object> hm = Map.of("success", true, "message", "Product updated successfully.");
            return ResponseEntity.ok().body(hm);
        }else {
            Map<String, Object> hm = Map.of("success", false, "message", "Product not found id: " + productUpdateRequestDto.getId() + "");
            return ResponseEntity.status(404).body(hm);
        }
    }

    // add cache to productList
    @Cacheable(value = "productListCache", key = "#page")
    public Page<Product> productList(int page){
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        System.out.println("Product - " +  model.hashCode());
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage;
    }

    public Page<Product> search(String q, int page, String price){
        Sort sort = Sort.by( price.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "price");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Product> productPage = productRepository.findByTitleContainsOrDescriptionContainsAllIgnoreCase(q, q, pageable);
        return productPage;
    }

    public Product getOne(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()){
            return optionalProduct.get();
        }
        throw new RuntimeException("Product not found id: " + id);
    }

}
