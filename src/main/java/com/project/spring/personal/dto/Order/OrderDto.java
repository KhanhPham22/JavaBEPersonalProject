package com.project.spring.personal.dto.Order;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private double totalAmount;
    private String status; // NEW, PROCESSING, COMPLETED
}
