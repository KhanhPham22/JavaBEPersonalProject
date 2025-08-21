package com.project.spring.personal.repository.chatbot;

import com.project.spring.personal.entity.chatbot.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
}
