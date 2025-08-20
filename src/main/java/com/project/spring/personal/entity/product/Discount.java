package com.project.spring.personal.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // mã giảm giá

    private BigDecimal percentage; // % giảm (vd: 0.10 = 10%)

    private BigDecimal amount; // giảm trực tiếp tiền

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean active;

    // Áp dụng cho sản phẩm nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}

