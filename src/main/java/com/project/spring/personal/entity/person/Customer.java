package com.project.spring.personal.entity.person;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "customers")
public class Customer extends User {

    private String address;
    private String phoneNumber;
    private int loyaltyPoints; // điểm tích lũy
}
