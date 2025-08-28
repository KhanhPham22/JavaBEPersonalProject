package com.project.spring.personal.service.payment;

import com.project.spring.personal.dto.Payment.PaymentDto;
import com.project.spring.personal.entity.order.Order;
import com.project.spring.personal.entity.payment.Payment;
import com.project.spring.personal.entity.payment.PaymentMethod;
import com.project.spring.personal.repository.order.OrderRepository;
import com.project.spring.personal.repository.payment.PaymentMethodRepository;
import com.project.spring.personal.repository.payment.PaymentRepository;
import com.project.spring.personal.service.Payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentDto paymentDto;
    private Order order;
    private PaymentMethod paymentMethod;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentDto = new PaymentDto();
        paymentDto.setOrderId(1L);
        paymentDto.setAmount(50.0);
        paymentDto.setStatus("PENDING");
        paymentDto.setMethod("COD");

        order = new Order();
        order.setId(1L);

        paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setMethodName("COD");

        payment = new Payment();
        payment.setId(1L);
        payment.setAmount(BigDecimal.valueOf(50.0));
        payment.setStatus("PENDING");
        payment.setPaidAt(LocalDateTime.now());
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
    }

    @Test
    void createPayment_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentMethodRepository.findAll()).thenReturn(Arrays.asList(paymentMethod));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.createPayment(paymentDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getStatus());
        assertEquals(order, result.getOrder());
        assertEquals(paymentMethod, result.getPaymentMethod());
        verify(orderRepository, times(1)).findById(1L);
        verify(paymentMethodRepository, times(1)).findAll();
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void createPayment_OrderNotFound_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.createPayment(paymentDto);
        });

        assertEquals("Order not found with ID: 1", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(paymentMethodRepository, never()).findAll();
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void createPayment_PaymentMethodNotFound_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentMethodRepository.findAll()).thenReturn(Arrays.asList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.createPayment(paymentDto);
        });

        assertEquals("Payment method not found: COD", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(paymentMethodRepository, times(1)).findAll();
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void getPaymentById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void getPaymentById_NotFound_ThrowsException() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.getPaymentById(1L);
        });

        assertEquals("Payment not found with ID: 1", exception.getMessage());
        verify(paymentRepository, times(1)).findById(1L);
    }
}
