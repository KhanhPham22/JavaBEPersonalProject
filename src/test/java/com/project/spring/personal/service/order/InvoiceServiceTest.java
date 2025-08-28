package com.project.spring.personal.service.order;

import com.project.spring.personal.dto.Order.InvoiceDto;
import com.project.spring.personal.entity.order.Invoice;
import com.project.spring.personal.entity.order.Order;
import com.project.spring.personal.repository.order.InvoiceRepository;
import com.project.spring.personal.repository.order.OrderRepository;
import com.project.spring.personal.service.Order.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private InvoiceDto invoiceDto;
    private Order order;
    private Invoice invoice;

    @BeforeEach
    void setUp() {
        invoiceDto = new InvoiceDto();
        invoiceDto.setOrderId(1L);

        order = new Order();
        order.setId(1L);

        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setAmount(BigDecimal.ZERO);
        invoice.setPaymentStatus("UNPAID");
        invoice.setOrder(order);
    }

    @Test
    void createInvoice_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        Invoice result = invoiceService.createInvoice(invoiceDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("UNPAID", result.getPaymentStatus());
        assertEquals(order, result.getOrder());
        verify(orderRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void createInvoice_OrderNotFound_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            invoiceService.createInvoice(invoiceDto);
        });

        assertEquals("Order not found with ID: 1", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void getInvoiceById_Success() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.getInvoiceById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(invoiceRepository, times(1)).findById(1L);
    }

    @Test
    void getInvoiceById_NotFound_ThrowsException() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            invoiceService.getInvoiceById(1L);
        });

        assertEquals("Invoice not found with ID: 1", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(1L);
    }
}
