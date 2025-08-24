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
@Table(name = "admins")
public class Admin extends User {

    private String adminCode;  // ví dụ mã quản trị viên
}
