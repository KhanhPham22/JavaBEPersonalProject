package com.project.spring.personal.service.Order;

import com.project.spring.personal.dto.Order.InvoiceDto;
import com.project.spring.personal.entity.order.Invoice;
import com.project.spring.personal.entity.order.Order;
import com.project.spring.personal.repository.order.InvoiceRepository;
import com.project.spring.personal.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Invoice createInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setAmount(BigDecimal.ZERO); // Default, adjust as needed
        invoice.setPaymentStatus("UNPAID");

        if (invoiceDto.getOrderId() != null) {
            Order order = orderRepository.findById(invoiceDto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + invoiceDto.getOrderId()));
            invoice.setOrder(order);
        }

        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + id));
    }
}