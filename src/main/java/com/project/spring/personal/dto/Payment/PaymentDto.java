package com.project.spring.personal.dto.Payment;

import lombok.Data;

@Data
public class PaymentDto {
    private Long id;
    private String status; // SUCCESS, FAILED, PENDING
    private double amount;
    private String method; // VISA, COD, PAYPAL...
    private Long orderId;
}
