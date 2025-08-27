package com.project.spring.personal.dto.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountDto {
    private Long id;
    private String code;
    private double percentage;
    private String description;
    private Long productId;
}
