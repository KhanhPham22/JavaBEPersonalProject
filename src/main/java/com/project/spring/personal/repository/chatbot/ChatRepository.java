package com.project.spring.personal.repository.chatbot;

import com.project.spring.personal.entity.chatbot.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
