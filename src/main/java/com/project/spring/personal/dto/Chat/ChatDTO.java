package com.project.spring.personal.dto.Chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private Long id;
    private String sender; // "CUSTOMER" or "BOT"
    private String message;
    private LocalDateTime sentAt;
}
