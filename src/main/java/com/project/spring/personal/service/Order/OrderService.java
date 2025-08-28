package com.project.spring.personal.service.Order;

import com.project.spring.personal.dto.Order.OrderDto;
import com.project.spring.personal.entity.order.Order;
import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.repository.order.OrderRepository;
import com.project.spring.personal.repository.person.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Order createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(orderDto.getStatus());
        order.setTotalAmount(BigDecimal.valueOf(orderDto.getTotalAmount()));

        if (orderDto.getUserId() != null) {
            Customer customer = customerRepository.findById(orderDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + orderDto.getUserId()));
            order.setCustomer(customer);
        }

        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }
}