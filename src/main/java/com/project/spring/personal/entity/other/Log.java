package com.project.spring.personal.entity.other;

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

    private String action; // LOGIN, CREATE_ORDER, PAYMENT_SUCCESS ...

    private String details;

    private LocalDateTime timestamp;

    private String ipAddress;

    // Ai thực hiện log (có thể null nếu là hệ thống)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
