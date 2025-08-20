package com.project.spring.personal.entity.product;

import com.project.spring.personal.entity.person.Customer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người dùng thích sản phẩm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Sản phẩm được yêu thích
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}