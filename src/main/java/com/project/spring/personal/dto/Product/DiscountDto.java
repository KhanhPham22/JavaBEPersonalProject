package com.project.spring.personal.dto.Product;

import lombok.Data;

@Data
public class DiscountDto {
    private Long id;
    private String code;
    private double percentage;
    private String description;
}
