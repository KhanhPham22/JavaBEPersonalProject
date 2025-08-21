package com.project.spring.personal.repository.product;

import com.project.spring.personal.entity.product.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
