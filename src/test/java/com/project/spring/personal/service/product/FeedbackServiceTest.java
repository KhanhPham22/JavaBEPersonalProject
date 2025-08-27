package com.project.spring.personal.service.product;

import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.product.Feedback;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.product.FeedbackRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.dto.Product.FeedbackDto;
import com.project.spring.personal.service.Product.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback feedback;
    private FeedbackDto feedbackDto;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Smartphone")
                .build();

        feedback = Feedback.builder()
                .id(1L)
                .message("Great product!")
                .createdAt(LocalDateTime.now())
                .customer(customer)
                .product(product)
                .build();

        feedbackDto = FeedbackDto.builder()
                .id(1L)
                .content("Great product!")
                .userId(1L)
                .productId(1L)
                .build();
    }

    @Test
    void createFeedback_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        FeedbackDto result = feedbackService.createFeedback(feedbackDto);

        assertNotNull(result);
        assertEquals(feedbackDto.getContent(), result.getContent());
        assertEquals(feedbackDto.getUserId(), result.getUserId());
        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void createFeedback_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> feedbackService.createFeedback(feedbackDto));
        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, never()).findById(any());
        verify(feedbackRepository, never()).save(any(Feedback.class));
    }

    @Test
    void updateFeedback_Success() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        FeedbackDto result = feedbackService.updateFeedback(1L, feedbackDto);

        assertNotNull(result);
        assertEquals(feedbackDto.getContent(), result.getContent());
        assertEquals(feedbackDto.getUserId(), result.getUserId());
        verify(feedbackRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void updateFeedback_NotFound() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> feedbackService.updateFeedback(1L, feedbackDto));
        verify(feedbackRepository, times(1)).findById(1L);
        verify(customerRepository, never()).findById(any());
        verify(productRepository, never()).findById(any());
        verify(feedbackRepository, never()).save(any(Feedback.class));
    }

    @Test
    void deleteFeedback_Success() {
        when(feedbackRepository.existsById(1L)).thenReturn(true);

        feedbackService.deleteFeedback(1L);

        verify(feedbackRepository, times(1)).existsById(1L);
        verify(feedbackRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFeedback_NotFound() {
        when(feedbackRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> feedbackService.deleteFeedback(1L));
        verify(feedbackRepository, times(1)).existsById(1L);
        verify(feedbackRepository, never()).deleteById(1L);
    }

    @Test
    void getFeedbackById_Success() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        FeedbackDto result = feedbackService.getFeedbackById(1L);

        assertNotNull(result);
        assertEquals(feedback.getId(), result.getId());
        assertEquals(feedback.getMessage(), result.getContent());
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void getFeedbackById_NotFound() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> feedbackService.getFeedbackById(1L));
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void getAllFeedbacks_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Feedback> page = new PageImpl<>(Arrays.asList(feedback));
        when(feedbackRepository.findAll(pageable)).thenReturn(page);

        Page<FeedbackDto> result = feedbackService.getAllFeedbacks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(feedback.getMessage(), result.getContent().get(0).getContent());
        verify(feedbackRepository, times(1)).findAll(pageable);
    }
}
