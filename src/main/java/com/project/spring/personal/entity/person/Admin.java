package com.project.spring.personal.entity.person;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admins")
public class Admin extends User {

    private String adminCode;  // ví dụ mã quản trị viên
}
