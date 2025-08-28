package com.project.spring.personal.controller.Payment;

import com.project.spring.personal.dto.Payment.PaymentDto;
import com.project.spring.personal.entity.payment.Payment;
import com.project.spring.personal.service.Payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        Payment payment = paymentService.createPayment(paymentDto);
        return ResponseEntity.ok(convertToDto(payment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(convertToDto(payment));
    }

    private PaymentDto convertToDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setStatus(payment.getStatus());
        dto.setAmount(payment.getAmount().doubleValue());
        dto.setMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getMethodName() : null);
        dto.setOrderId(payment.getOrder() != null ? payment.getOrder().getId() : null);
        return dto;
    }
}