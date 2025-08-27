package com.project.spring.personal.dto.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteItemDto {
    private Long id;
    private Long userId;
    private Long productId;
}
