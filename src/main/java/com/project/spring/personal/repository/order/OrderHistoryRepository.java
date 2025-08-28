package com.project.spring.personal.repository.order;

import com.project.spring.personal.entity.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}

