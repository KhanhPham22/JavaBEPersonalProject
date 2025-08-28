package com.project.spring.personal.service.payment;

import com.project.spring.personal.dto.Payment.PaymentMethodDto;
import com.project.spring.personal.entity.payment.PaymentMethod;
import com.project.spring.personal.repository.payment.PaymentMethodRepository;
import com.project.spring.personal.service.Payment.PaymentMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    private PaymentMethodDto paymentMethodDto;
    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        paymentMethodDto = new PaymentMethodDto();
        paymentMethodDto.setName("COD");
        paymentMethodDto.setDescription("Cash on Delivery");

        paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setMethodName("COD");
        paymentMethod.setDescription("Cash on Delivery");
        paymentMethod.setActive(true);
    }

    @Test
    void createPaymentMethod_Success() {
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethod result = paymentMethodService.createPaymentMethod(paymentMethodDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("COD", result.getMethodName());
        assertTrue(result.isActive());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void getPaymentMethodById_Success() {
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));

        PaymentMethod result = paymentMethodService.getPaymentMethodById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(paymentMethodRepository, times(1)).findById(1L);
    }

    @Test
    void getPaymentMethodById_NotFound_ThrowsException() {
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentMethodService.getPaymentMethodById(1L);
        });

        assertEquals("Payment method not found with ID: 1", exception.getMessage());
        verify(paymentMethodRepository, times(1)).findById(1L);
    }
}
