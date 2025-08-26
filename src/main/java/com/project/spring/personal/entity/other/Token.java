package com.project.spring.personal.entity.other;

import com.project.spring.personal.entity.person.Role;
import com.project.spring.personal.entity.person.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private boolean revoked;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;
}
