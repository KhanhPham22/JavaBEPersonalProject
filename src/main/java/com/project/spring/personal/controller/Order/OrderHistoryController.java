package com.project.spring.personal.controller.Order;

import com.project.spring.personal.dto.Order.OrderHistoryDto;
import com.project.spring.personal.entity.order.OrderHistory;
import com.project.spring.personal.service.Order.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-histories")
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    @PostMapping
    public ResponseEntity<OrderHistoryDto> createOrderHistory(@RequestBody OrderHistoryDto orderHistoryDto) {
        OrderHistory orderHistory = orderHistoryService.createOrderHistory(orderHistoryDto);
        return ResponseEntity.ok(convertToDto(orderHistory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderHistoryDto> getOrderHistoryById(@PathVariable Long id) {
        OrderHistory orderHistory = orderHistoryService.getOrderHistoryById(id);
        return ResponseEntity.ok(convertToDto(orderHistory));
    }

    private OrderHistoryDto convertToDto(OrderHistory orderHistory) {
        OrderHistoryDto dto = new OrderHistoryDto();
        dto.setId(orderHistory.getId());
        dto.setOrderId(orderHistory.getOrder() != null ? orderHistory.getOrder().getId() : null);
        dto.setAction(orderHistory.getStatus());
        dto.setTimestamp(orderHistory.getChangedAt().toString());
        return dto;
    }
}