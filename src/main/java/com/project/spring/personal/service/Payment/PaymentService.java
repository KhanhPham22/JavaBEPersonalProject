package com.project.spring.personal.service.Payment;

import com.project.spring.personal.dto.Payment.PaymentDto;
import com.project.spring.personal.entity.order.Order;
import com.project.spring.personal.entity.payment.Payment;
import com.project.spring.personal.entity.payment.PaymentMethod;
import com.project.spring.personal.repository.payment.PaymentRepository;
import com.project.spring.personal.repository.order.OrderRepository;
import com.project.spring.personal.repository.payment.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public Payment createPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(paymentDto.getAmount()));
        payment.setStatus(paymentDto.getStatus());
        payment.setPaidAt(LocalDateTime.now());

        if (paymentDto.getOrderId() != null) {
            Order order = orderRepository.findById(paymentDto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + paymentDto.getOrderId()));
            payment.setOrder(order);
        }

        if (paymentDto.getMethod() != null) {
            PaymentMethod paymentMethod = paymentMethodRepository.findAll().stream()
                    .filter(pm -> pm.getMethodName().equals(paymentDto.getMethod()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Payment method not found: " + paymentDto.getMethod()));
            payment.setPaymentMethod(paymentMethod);
        }

        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
    }
}