package com.project.spring.personal.controller.Order;

import com.project.spring.personal.dto.Order.OrderDto;
import com.project.spring.personal.entity.order.Order;
import com.project.spring.personal.service.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderService.createOrder(orderDto);
        return ResponseEntity.ok(convertToDto(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(convertToDto(order));
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getCustomer() != null ? order.getCustomer().getId() : null);
        dto.setTotalAmount(order.getTotalAmount().doubleValue());
        dto.setStatus(order.getStatus());
        return dto;
    }
}