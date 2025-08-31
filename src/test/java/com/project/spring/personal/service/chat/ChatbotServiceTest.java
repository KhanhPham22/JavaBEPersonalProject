package com.project.spring.personal.service.chat;

import com.project.spring.personal.dto.Chat.ChatDTO;
import com.project.spring.personal.dto.Chat.ChatHistoryDTO;
import com.project.spring.personal.entity.chatbot.Chat;
import com.project.spring.personal.entity.chatbot.ChatHistory;
import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.repository.chatbot.ChatHistoryRepository;
import com.project.spring.personal.repository.chatbot.ChatRepository;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.service.Chat.ChatbotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatbotServiceTest {

    @Mock
    private ChatHistoryRepository chatHistoryRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ChatbotService chatbotService;

    private Customer customer;
    private ChatHistory chatHistory;
    private Chat chat;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        customer = Customer.builder()
                .id(1L)
                .email("test@example.com")
                .build();
        chatHistory = ChatHistory.builder()
                .id(1L)
                .startedAt(now)
                .customer(customer)
                .chats(new HashSet<>())
                .build();
        chat = Chat.builder()
                .id(1L)
                .sender("CUSTOMER")
                .message("Hello, bot!")
                .sentAt(now)
                .chatHistory(chatHistory)
                .build();
    }

    @Test
    void testCreateChatHistory_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(chatHistoryRepository.save(any(ChatHistory.class))).thenReturn(chatHistory);

        // Act
        ChatHistoryDTO result = chatbotService.createChatHistory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(now, result.getStartedAt());
        assertEquals(1L, result.getCustomerId());
        assertTrue(result.getChats().isEmpty());
        verify(customerRepository, times(1)).findById(1L);
        verify(chatHistoryRepository, times(1)).save(any(ChatHistory.class));
    }

    @Test
    void testCreateChatHistory_CustomerNotFound() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> chatbotService.createChatHistory(1L));
        assertEquals("Customer not found with ID: 1", exception.getMessage());
        verify(customerRepository, times(1)).findById(1L);
        verify(chatHistoryRepository, never()).save(any(ChatHistory.class));
    }

    @Test
    void testSendMessage_Success() {
        // Arrange
        ChatDTO chatDTO = ChatDTO.builder()
                .sender("CUSTOMER")
                .message("Hello, bot!")
                .build();
        when(chatHistoryRepository.findById(1L)).thenReturn(Optional.of(chatHistory));
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        // Act
        ChatDTO result = chatbotService.sendMessage(1L, chatDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CUSTOMER", result.getSender());
        assertEquals("Hello, bot!", result.getMessage());
        assertEquals(now, result.getSentAt());
        verify(chatHistoryRepository, times(1)).findById(1L);
        verify(chatRepository, times(1)).save(any(Chat.class));
    }

    @Test
    void testSendMessage_ChatHistoryNotFound() {
        // Arrange
        ChatDTO chatDTO = ChatDTO.builder()
                .sender("CUSTOMER")
                .message("Hello, bot!")
                .build();
        when(chatHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> chatbotService.sendMessage(1L, chatDTO));
        assertEquals("ChatHistory not found with ID: 1", exception.getMessage());
        verify(chatHistoryRepository, times(1)).findById(1L);
        verify(chatRepository, never()).save(any(Chat.class));
    }

    @Test
    void testGetChatHistory_Success() {
        // Arrange
        chatHistory.getChats().add(chat);
        when(chatHistoryRepository.findById(1L)).thenReturn(Optional.of(chatHistory));

        // Act
        ChatHistoryDTO result = chatbotService.getChatHistory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(now, result.getStartedAt());
        assertEquals(1L, result.getCustomerId());
        assertEquals(1, result.getChats().size());
        ChatDTO chatDTO = result.getChats().iterator().next();
        assertEquals("CUSTOMER", chatDTO.getSender());
        assertEquals("Hello, bot!", chatDTO.getMessage());
        verify(chatHistoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetChatHistory_NotFound() {
        // Arrange
        when(chatHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> chatbotService.getChatHistory(1L));
        assertEquals("ChatHistory not found with ID: 1", exception.getMessage());
        verify(chatHistoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetChatHistoriesByCustomer_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(chatHistoryRepository.findAll()).thenReturn(List.of(chatHistory));

        // Act
        Set<ChatHistoryDTO> result = chatbotService.getChatHistoriesByCustomer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        ChatHistoryDTO chatHistoryDTO = result.iterator().next();
        assertEquals(1L, chatHistoryDTO.getId());
        assertEquals(1L, chatHistoryDTO.getCustomerId());
        verify(customerRepository, times(1)).findById(1L);
        verify(chatHistoryRepository, times(1)).findAll();
    }

    @Test
    void testGetChatHistoriesByCustomer_CustomerNotFound() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> chatbotService.getChatHistoriesByCustomer(1L));
        assertEquals("Customer not found with ID: 1", exception.getMessage());
        verify(customerRepository, times(1)).findById(1L);
        verify(chatHistoryRepository, never()).findAll();
    }
}
