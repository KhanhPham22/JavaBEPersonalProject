package com.project.spring.personal.repository.payment;

import com.project.spring.personal.entity.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}