package com.works.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.works.entity.Product}
 */
@Data
public class ProductUpdateRequestDto {
    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    Long id;
    @NotNull
    @Size(min = 2, max = 100)
    @NotEmpty
    String title;
    @NotNull
    @Size(min = 2, max = 500)
    @NotEmpty
    String description;
    @NotNull
    @Min(1)
    @Max(1000000)
    BigDecimal price;
}