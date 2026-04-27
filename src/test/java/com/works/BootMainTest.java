package com.works;

import com.works.entity.Product;
import com.works.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
public class BootMainTest {

    @Autowired
    ProductService productService;

    @Test
    void contextLoads() {
        Page<Product> productPage = productService.productList(0);
        int size = productPage.getContent().size();
        Assertions.assertAll(
                () -> Assertions.assertTrue(size > 0, "Product list should not be empty"),
                () -> Assertions.assertEquals(size, 10, "Product list should contain 10 elements")
        );
    }

}
