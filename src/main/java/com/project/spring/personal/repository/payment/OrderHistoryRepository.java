package com.project.spring.personal.repository.payment;

import com.project.spring.personal.entity.payment.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}

