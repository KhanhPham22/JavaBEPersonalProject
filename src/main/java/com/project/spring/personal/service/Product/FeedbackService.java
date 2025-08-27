package com.project.spring.personal.service.Product;

import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.product.Feedback;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.product.FeedbackRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.dto.Product.FeedbackDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository) {
        this.feedbackRepository = feedbackRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public FeedbackDto createFeedback(FeedbackDto feedbackDto) {
        Customer customer = customerRepository.findById(feedbackDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + feedbackDto.getUserId()));
        Product product = productRepository.findById(feedbackDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + feedbackDto.getProductId()));

        Feedback feedback = Feedback.builder()
                .message(feedbackDto.getContent())
                .createdAt(LocalDateTime.now())
                .customer(customer)
                .product(product)
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return convertToDto(savedFeedback);
    }

    public FeedbackDto updateFeedback(Long id, FeedbackDto feedbackDto) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if (optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            Customer customer = customerRepository.findById(feedbackDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + feedbackDto.getUserId()));
            Product product = productRepository.findById(feedbackDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + feedbackDto.getProductId()));

            feedback.setMessage(feedbackDto.getContent());
            feedback.setCustomer(customer);
            feedback.setProduct(product);

            Feedback updatedFeedback = feedbackRepository.save(feedback);
            return convertToDto(updatedFeedback);
        }
        throw new RuntimeException("Feedback not found with id: " + id);
    }

    public void deleteFeedback(Long id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id);
        } else {
            throw new RuntimeException("Feedback not found with id: " + id);
        }
    }

    public FeedbackDto getFeedbackById(Long id) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if (optionalFeedback.isPresent()) {
            return convertToDto(optionalFeedback.get());
        }
        throw new RuntimeException("Feedback not found with id: " + id);
    }

    public Page<FeedbackDto> getAllFeedbacks(Pageable pageable) {
        return feedbackRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    private FeedbackDto convertToDto(Feedback feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .content(feedback.getMessage())
                .productId(feedback.getProduct().getId())
                .userId(feedback.getCustomer().getId())
                .build();
    }
}