package com.project.spring.personal.repository.order;

import com.project.spring.personal.entity.order.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
