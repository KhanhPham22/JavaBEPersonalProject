package com.project.spring.personal.service.product;

import com.project.spring.personal.entity.product.Discount;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.repository.product.DiscountRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.dto.Product.DiscountDto;
import com.project.spring.personal.service.Product.DiscountService;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DiscountService discountService;

    private Discount discount;
    private DiscountDto discountDto;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Smartphone")
                .build();

        discount = Discount.builder()
                .id(1L)
                .code("DISCOUNT10")
                .percentage(BigDecimal.valueOf(10.0))
                .description("10% off") // Added description to match updated Discount entity
                .product(product)
                .active(true)
                .build();

        discountDto = DiscountDto.builder()
                .id(1L)
                .code("DISCOUNT10")
                .percentage(10.0)
                .description("10% off")
                .productId(1L)
                .build();
    }

    @Test
    void createDiscount_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(discountRepository.save(any(Discount.class))).thenReturn(discount);

        DiscountDto result = discountService.createDiscount(discountDto);

        assertNotNull(result);
        assertEquals(discountDto.getCode(), result.getCode());
        assertEquals(discountDto.getPercentage(), result.getPercentage());
        assertEquals(discountDto.getDescription(), result.getDescription());
        assertEquals(discountDto.getProductId(), result.getProductId());
        verify(productRepository, times(1)).findById(1L);
        verify(discountRepository, times(1)).save(any(Discount.class));
    }

    @Test
    void createDiscount_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> discountService.createDiscount(discountDto));
        verify(productRepository, times(1)).findById(1L);
        verify(discountRepository, never()).save(any(Discount.class));
    }

    @Test
    void updateDiscount_Success() {
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(discountRepository.save(any(Discount.class))).thenReturn(discount);

        DiscountDto result = discountService.updateDiscount(1L, discountDto);

        assertNotNull(result);
        assertEquals(discountDto.getCode(), result.getCode());
        assertEquals(discountDto.getPercentage(), result.getPercentage());
        assertEquals(discountDto.getDescription(), result.getDescription());
        assertEquals(discountDto.getProductId(), result.getProductId());
        verify(discountRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(discountRepository, times(1)).save(any(Discount.class));
    }

    @Test
    void updateDiscount_NotFound() {
        when(discountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> discountService.updateDiscount(1L, discountDto));
        verify(discountRepository, times(1)).findById(1L);
        verify(productRepository, never()).findById(any());
        verify(discountRepository, never()).save(any(Discount.class));
    }

    @Test
    void deleteDiscount_Success() {
        when(discountRepository.existsById(1L)).thenReturn(true);

        discountService.deleteDiscount(1L);

        verify(discountRepository, times(1)).existsById(1L);
        verify(discountRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDiscount_NotFound() {
        when(discountRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> discountService.deleteDiscount(1L));
        verify(discountRepository, times(1)).existsById(1L);
        verify(discountRepository, never()).deleteById(1L);
    }

    @Test
    void getDiscountById_Success() {
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));

        DiscountDto result = discountService.getDiscountById(1L);

        assertNotNull(result);
        assertEquals(discount.getId(), result.getId());
        assertEquals(discount.getCode(), result.getCode());
        assertEquals(discount.getDescription(), result.getDescription());
        assertEquals(discount.getProduct().getId(), result.getProductId());
        verify(discountRepository, times(1)).findById(1L);
    }

    @Test
    void getDiscountById_NotFound() {
        when(discountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> discountService.getDiscountById(1L));
        verify(discountRepository, times(1)).findById(1L);
    }

    @Test
    void getAllDiscounts_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Discount> page = new PageImpl<>(Arrays.asList(discount));
        when(discountRepository.findAll(pageable)).thenReturn(page);

        Page<DiscountDto> result = discountService.getAllDiscounts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(discount.getCode(), result.getContent().get(0).getCode());
        assertEquals(discount.getDescription(), result.getContent().get(0).getDescription());
        assertEquals(discount.getProduct().getId(), result.getContent().get(0).getProductId());
        verify(discountRepository, times(1)).findAll(pageable);
    }
}