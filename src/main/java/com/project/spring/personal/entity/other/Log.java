package com.project.spring.personal.entity.other;

import com.project.spring.personal.entity.person.Role;
import com.project.spring.personal.entity.person.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // LOGIN, CREATE_ORDER, etc.
    private String details;
    private LocalDateTime timestamp;
    private String ipAddress;

    // Tham chiếu gián tiếp
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;
}

