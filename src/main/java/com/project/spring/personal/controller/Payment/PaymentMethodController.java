package com.project.spring.personal.controller.Payment;

import com.project.spring.personal.dto.Payment.PaymentMethodDto;
import com.project.spring.personal.entity.payment.PaymentMethod;
import com.project.spring.personal.service.Payment.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping
    public ResponseEntity<PaymentMethodDto> createPaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = paymentMethodService.createPaymentMethod(paymentMethodDto);
        return ResponseEntity.ok(convertToDto(paymentMethod));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
        return ResponseEntity.ok(convertToDto(paymentMethod));
    }

    private PaymentMethodDto convertToDto(PaymentMethod paymentMethod) {
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setId(paymentMethod.getId());
        dto.setName(paymentMethod.getMethodName());
        dto.setDescription(paymentMethod.getDescription());
        return dto;
    }
}