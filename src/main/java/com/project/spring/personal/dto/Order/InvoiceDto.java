package com.project.spring.personal.dto.Order;

import lombok.Data;

@Data
public class InvoiceDto {
    private Long id;
    private Long orderId;
    private String invoiceNumber;
    private String issueDate;
}
