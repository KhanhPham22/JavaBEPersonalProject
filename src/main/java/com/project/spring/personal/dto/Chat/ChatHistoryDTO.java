package com.project.spring.personal.dto.Chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatHistoryDTO {
    private Long id;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Long customerId;
    private Set<ChatDTO> chats;
}
