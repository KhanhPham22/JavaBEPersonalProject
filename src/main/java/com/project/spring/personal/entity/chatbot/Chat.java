package com.project.spring.personal.entity.chatbot;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender; // "CUSTOMER" hoặc "BOT"

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime sentAt;

    // Tin nhắn thuộc về phiên chat nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_history_id")
    private ChatHistory chatHistory;
}