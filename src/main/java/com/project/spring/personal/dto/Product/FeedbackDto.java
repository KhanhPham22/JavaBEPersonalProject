package com.project.spring.personal.dto.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackDto {
    private Long id;
    private String content;
    private Long productId;
    private Long userId;
}
