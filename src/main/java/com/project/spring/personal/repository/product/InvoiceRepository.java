package com.project.spring.personal.repository.product;

import com.project.spring.personal.entity.product.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
