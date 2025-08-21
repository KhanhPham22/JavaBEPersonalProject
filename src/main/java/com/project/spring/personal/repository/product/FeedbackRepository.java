package com.project.spring.personal.repository.product;

import com.project.spring.personal.entity.product.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
