package com.project.spring.personal.service.Payment;

import com.project.spring.personal.dto.Payment.PaymentMethodDto;
import com.project.spring.personal.entity.payment.PaymentMethod;
import com.project.spring.personal.repository.payment.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public PaymentMethod createPaymentMethod(PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethodName(paymentMethodDto.getName());
        paymentMethod.setDescription(paymentMethodDto.getDescription());
        paymentMethod.setActive(true);
        return paymentMethodRepository.save(paymentMethod);
    }

    public PaymentMethod getPaymentMethodById(Long id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with ID: " + id));
    }
}