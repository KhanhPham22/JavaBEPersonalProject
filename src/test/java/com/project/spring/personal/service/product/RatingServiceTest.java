package com.project.spring.personal.service.product;

import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.entity.product.Rating;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.repository.product.RatingRepository;
import com.project.spring.personal.dto.Product.RatingDto;
import com.project.spring.personal.service.Product.RatingService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private RatingService ratingService;

    private Rating rating;
    private RatingDto ratingDto;
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

        rating = Rating.builder()
                .id(1L)
                .stars(5)
                .comment("Excellent product!")
                .customer(customer)
                .product(product)
                .build();

        ratingDto = RatingDto.builder()
                .id(1L)
                .score(5)
                .comment("Excellent product!")
                .userId(1L)
                .productId(1L)
                .build();
    }

    @Test
    void createRating_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        RatingDto result = ratingService.createRating(ratingDto);

        assertNotNull(result);
        assertEquals(ratingDto.getScore(), result.getScore());
        assertEquals(ratingDto.getComment(), result.getComment());
        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void createRating_InvalidScore() {
        ratingDto.setScore(6);
        assertThrows(IllegalArgumentException.class, () -> ratingService.createRating(ratingDto));
        verify(customerRepository, never()).findById(any());
        verify(productRepository, never()).findById(any());
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void createRating_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ratingService.createRating(ratingDto));
        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, never()).findById(any());
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void updateRating_Success() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        RatingDto result = ratingService.updateRating(1L, ratingDto);

        assertNotNull(result);
        assertEquals(ratingDto.getScore(), result.getScore());
        assertEquals(ratingDto.getComment(), result.getComment());
        verify(ratingRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void updateRating_NotFound() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ratingService.updateRating(1L, ratingDto));
        verify(ratingRepository, times(1)).findById(1L);
        verify(customerRepository, never()).findById(any());
        verify(productRepository, never()).findById(any());
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void deleteRating_Success() {
        when(ratingRepository.existsById(1L)).thenReturn(true);

        ratingService.deleteRating(1L);

        verify(ratingRepository, times(1)).existsById(1L);
        verify(ratingRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRating_NotFound() {
        when(ratingRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ratingService.deleteRating(1L));
        verify(ratingRepository, times(1)).existsById(1L);
        verify(ratingRepository, never()).deleteById(1L);
    }

    @Test
    void getRatingById_Success() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));

        RatingDto result = ratingService.getRatingById(1L);

        assertNotNull(result);
        assertEquals(rating.getId(), result.getId());
        assertEquals(rating.getStars(), result.getScore());
        verify(ratingRepository, times(1)).findById(1L);
    }

    @Test
    void getRatingById_NotFound() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ratingService.getRatingById(1L));
        verify(ratingRepository, times(1)).findById(1L);
    }

    @Test
    void getAllRatings_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rating> page = new PageImpl<>(Arrays.asList(rating));
        when(ratingRepository.findAll(pageable)).thenReturn(page);

        Page<RatingDto> result = ratingService.getAllRatings(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(rating.getStars(), result.getContent().get(0).getScore());
        verify(ratingRepository, times(1)).findAll(pageable);
    }
}
