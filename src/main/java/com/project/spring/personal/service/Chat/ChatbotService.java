package com.project.spring.personal.service.Chat;

import com.project.spring.personal.dto.Chat.ChatDTO;
import com.project.spring.personal.dto.Chat.ChatHistoryDTO;
import com.project.spring.personal.entity.chatbot.Chat;
import com.project.spring.personal.entity.chatbot.ChatHistory;
import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.repository.chatbot.ChatHistoryRepository;
import com.project.spring.personal.repository.chatbot.ChatRepository;
import com.project.spring.personal.repository.person.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public ChatHistoryDTO createChatHistory(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

        ChatHistory chatHistory = ChatHistory.builder()
                .customer(customer)
                .startedAt(LocalDateTime.now())
                .build();

        chatHistory = chatHistoryRepository.save(chatHistory);
        return convertToChatHistoryDTO(chatHistory);
    }

    public ChatDTO sendMessage(Long chatHistoryId, ChatDTO chatDTO) {
        ChatHistory chatHistory = chatHistoryRepository.findById(chatHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("ChatHistory not found with ID: " + chatHistoryId));

        Chat chat = Chat.builder()
                .sender(chatDTO.getSender())
                .message(chatDTO.getMessage())
                .sentAt(LocalDateTime.now())
                .chatHistory(chatHistory)
                .build();

        chat = chatRepository.save(chat);
        return convertToChatDTO(chat);
    }

    public ChatHistoryDTO getChatHistory(Long chatHistoryId) {
        ChatHistory chatHistory = chatHistoryRepository.findById(chatHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("ChatHistory not found with ID: " + chatHistoryId));
        return convertToChatHistoryDTO(chatHistory);
    }

    public Set<ChatHistoryDTO> getChatHistoriesByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
        return chatHistoryRepository.findAll().stream()
                .filter(ch -> ch.getCustomer().getId().equals(customerId))
                .map(this::convertToChatHistoryDTO)
                .collect(Collectors.toSet());
    }

    private ChatDTO convertToChatDTO(Chat chat) {
        return ChatDTO.builder()
                .id(chat.getId())
                .sender(chat.getSender())
                .message(chat.getMessage())
                .sentAt(chat.getSentAt())
                .build();
    }

    private ChatHistoryDTO convertToChatHistoryDTO(ChatHistory chatHistory) {
        return ChatHistoryDTO.builder()
                .id(chatHistory.getId())
                .startedAt(chatHistory.getStartedAt())
                .endedAt(chatHistory.getEndedAt())
                .customerId(chatHistory.getCustomer().getId())
                .chats(chatHistory.getChats().stream()
                        .map(this::convertToChatDTO)
                        .collect(Collectors.toSet()))
                .build();
    }
}
