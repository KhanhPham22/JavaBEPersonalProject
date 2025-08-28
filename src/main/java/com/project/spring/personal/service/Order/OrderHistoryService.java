package com.project.spring.personal.service.Order;

import com.project.spring.personal.dto.Order.OrderHistoryDto;
import com.project.spring.personal.entity.order.Order;
import com.project.spring.personal.entity.order.OrderHistory;
import com.project.spring.personal.repository.order.OrderHistoryRepository;
import com.project.spring.personal.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderHistoryService {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public OrderHistory createOrderHistory(OrderHistoryDto orderHistoryDto) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setStatus(orderHistoryDto.getAction());
        orderHistory.setChangedAt(LocalDateTime.now());
        orderHistory.setNote("Status changed to " + orderHistoryDto.getAction());

        if (orderHistoryDto.getOrderId() != null) {
            Order order = orderRepository.findById(orderHistoryDto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderHistoryDto.getOrderId()));
            orderHistory.setOrder(order);
        }

        return orderHistoryRepository.save(orderHistory);
    }

    public OrderHistory getOrderHistoryById(Long id) {
       // ismo
        return orderHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order history not found with ID: " + id));
    }
}