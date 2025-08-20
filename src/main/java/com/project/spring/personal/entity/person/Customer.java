package com.project.spring.personal.entity.person;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    private Long id; // trùng với User.id

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private int loyaltyPoints; // điểm tích luỹ
}
