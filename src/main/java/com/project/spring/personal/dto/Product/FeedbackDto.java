package com.project.spring.personal.dto.Product;

import lombok.Data;

@Data
public class FeedbackDto {
    private Long id;
    private String content;
    private Long productId;
    private Long userId;
}
