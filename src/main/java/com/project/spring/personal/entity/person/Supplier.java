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
@Table(name = "suppliers")
public class Supplier extends User {

    private String companyName;
    private String taxCode;
}