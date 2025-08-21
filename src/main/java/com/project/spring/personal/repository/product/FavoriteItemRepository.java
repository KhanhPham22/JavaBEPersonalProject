package com.project.spring.personal.repository.product;

import com.project.spring.personal.entity.product.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
}
