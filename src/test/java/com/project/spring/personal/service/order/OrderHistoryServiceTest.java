package com.project.spring.personal.service.order;

import com.project.spring.personal.dto.Order.OrderHistoryDto;
import com.project.spring.personal.entity.order.Order;
import com.project.spring.personal.entity.order.OrderHistory;
import com.project.spring.personal.repository.order.OrderHistoryRepository;
import com.project.spring.personal.repository.order.OrderRepository;
import com.project.spring.personal.service.Order.OrderHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderHistoryServiceTest {

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderHistoryService orderHistoryService;

    private OrderHistoryDto orderHistoryDto;
    private Order order;
    private OrderHistory orderHistory;

    @BeforeEach
    void setUp() {
        orderHistoryDto = new OrderHistoryDto();
        orderHistoryDto.setOrderId(1L);
        orderHistoryDto.setAction("CREATED");

        order = new Order();
        order.setId(1L);

        orderHistory = new OrderHistory();
        orderHistory.setId(1L);
        orderHistory.setStatus("CREATED");
        orderHistory.setChangedAt(LocalDateTime.now());
        orderHistory.setNote("Status changed to CREATED");
        orderHistory.setOrder(order);
    }

    @Test
    void createOrderHistory_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderHistoryRepository.save(any(OrderHistory.class))).thenReturn(orderHistory);

        OrderHistory result = orderHistoryService.createOrderHistory(orderHistoryDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CREATED", result.getStatus());
        assertEquals(order, result.getOrder());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderHistoryRepository, times(1)).save(any(OrderHistory.class));
    }

    @Test
    void createOrderHistory_OrderNotFound_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderHistoryService.createOrderHistory(orderHistoryDto);
        });

        assertEquals("Order not found with ID: 1", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderHistoryRepository, never()).save(any());
    }

    @Test
    void getOrderHistoryById_Success() {
        when(orderHistoryRepository.findById(1L)).thenReturn(Optional.of(orderHistory));

        OrderHistory result = orderHistoryService.getOrderHistoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderHistoryRepository, times(1)).findById(1L);
    }

    @Test
    void getOrderHistoryById_NotFound_ThrowsException() {
        when(orderHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderHistoryService.getOrderHistoryById(1L);
        });

        assertEquals("Order history not found with ID: 1", exception.getMessage());
        verify(orderHistoryRepository, times(1)).findById(1L);
    }
}
