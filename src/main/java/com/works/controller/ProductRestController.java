package com.works.controller;

import com.works.dto.ProductSaveRequestDto;
import com.works.dto.ProductUpdateRequestDto;
import com.works.entity.Product;
import com.works.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductRestController {

    final ProductService productService;

    @PostMapping("save")
    public Product save(@Valid @RequestBody ProductSaveRequestDto productSaveRequestDto){
        return productService.save(productSaveRequestDto);
    }

    @PostMapping("saveAll")
    public List<Product> saveAll(@Valid @RequestBody List<ProductSaveRequestDto> productSaveRequestDtos){
        return productService.saveAll(productSaveRequestDtos);
    }

    @DeleteMapping("deleteOne/{id}")
    public ResponseEntity deleteOne(@PathVariable Long id){
        return productService.deleteOne(id);
    }

    @PutMapping("update")
    public ResponseEntity update(@Valid @RequestBody ProductUpdateRequestDto productUpdateRequestDto){
        return productService.update(productUpdateRequestDto);
    }

    @GetMapping("list")
    public Page<Product> productList(
            @RequestParam(defaultValue = "0") int page
    ){
        return productService.productList(page);
    }

    @GetMapping("search")
    public Page<Product> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "asc") String price
    ){
        return productService.search(q, page, price);
    }

}
