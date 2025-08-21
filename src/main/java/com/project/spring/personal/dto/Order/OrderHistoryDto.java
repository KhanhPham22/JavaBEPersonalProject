package com.project.spring.personal.dto.Order;

import lombok.Data;

@Data
public class OrderHistoryDto {
    private Long id;
    private Long orderId;
    private String action; // CREATED, UPDATED, CANCELLED
    private String timestamp;
}