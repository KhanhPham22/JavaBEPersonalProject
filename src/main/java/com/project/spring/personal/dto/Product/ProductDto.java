package com.project.spring.personal.dto.Product;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private Long categoryId;
}
