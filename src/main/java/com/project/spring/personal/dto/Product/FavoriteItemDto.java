package com.project.spring.personal.dto.Product;

import lombok.Data;

@Data
public class FavoriteItemDto {
    private Long id;
    private Long userId;
    private Long productId;
}
