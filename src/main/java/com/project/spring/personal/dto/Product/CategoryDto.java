package com.project.spring.personal.dto.Product;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}
