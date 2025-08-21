package com.project.spring.personal.repository.product;

import com.project.spring.personal.entity.product.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
