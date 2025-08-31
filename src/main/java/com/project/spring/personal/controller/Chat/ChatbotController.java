package com.project.spring.personal.controller.Chat;

import com.project.spring.personal.dto.Chat.ChatDTO;
import com.project.spring.personal.dto.Chat.ChatHistoryDTO;
import com.project.spring.personal.service.Chat.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/history/{customerId}")
    public ResponseEntity<ChatHistoryDTO> createChatHistory(@PathVariable Long customerId) {
        ChatHistoryDTO chatHistoryDTO = chatbotService.createChatHistory(customerId);
        return ResponseEntity.ok(chatHistoryDTO);
    }

    @PostMapping("/message/{chatHistoryId}")
    public ResponseEntity<ChatDTO> sendMessage(@PathVariable Long chatHistoryId, @RequestBody ChatDTO chatDTO) {
        ChatDTO savedChatDTO = chatbotService.sendMessage(chatHistoryId, chatDTO);
        return ResponseEntity.ok(savedChatDTO);
    }

    @GetMapping("/history/{chatHistoryId}")
    public ResponseEntity<ChatHistoryDTO> getChatHistory(@PathVariable Long chatHistoryId) {
        ChatHistoryDTO chatHistoryDTO = chatbotService.getChatHistory(chatHistoryId);
        return ResponseEntity.ok(chatHistoryDTO);
    }

    @GetMapping("/histories/customer/{customerId}")
    public ResponseEntity<Set<ChatHistoryDTO>> getChatHistoriesByCustomer(@PathVariable Long customerId) {
        Set<ChatHistoryDTO> chatHistories = chatbotService.getChatHistoriesByCustomer(customerId);
        return ResponseEntity.ok(chatHistories);
    }
}
