package com.works.controller;

import com.works.dto.ProductSaveRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;  // sadece JUnit 5
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestProductRestControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void saveTest() {
        ProductSaveRequestDto dto = new ProductSaveRequestDto();
        dto.setTitle("Test Product");
        dto.setPrice(new BigDecimal("19.99"));  // String constructor tercih edilmeli
        dto.setDescription("Test Description");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/product/save")
                .then()
                .statusCode(200);
    }

}
