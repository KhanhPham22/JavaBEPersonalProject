package com.project.spring.personal.dto.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingDto {
    private Long id;
    private int score; // 1-5
    private String comment;
    private Long productId;
    private Long userId;
}
