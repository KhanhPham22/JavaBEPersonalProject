package com.project.spring.personal.controller.Order;

import com.project.spring.personal.dto.Order.InvoiceDto;
import com.project.spring.personal.entity.order.Invoice;
import com.project.spring.personal.service.Order.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        Invoice invoice = invoiceService.createInvoice(invoiceDto);
        return ResponseEntity.ok(convertToDto(invoice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(convertToDto(invoice));
    }

    private InvoiceDto convertToDto(Invoice invoice) {
        InvoiceDto dto = new InvoiceDto();
        dto.setId(invoice.getId());
        dto.setOrderId(invoice.getOrder() != null ? invoice.getOrder().getId() : null);
        dto.setInvoiceNumber("INV-" + invoice.getId());
        dto.setIssueDate(invoice.getIssuedAt().toString());
        return dto;
    }
}