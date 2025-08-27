package com.project.spring.personal.service.product;

import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.product.FavoriteItem;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.product.FavoriteItemRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.dto.Product.FavoriteItemDto;
import com.project.spring.personal.service.Product.FavoriteItemService;
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
class FavoriteItemServiceTest {

    @Mock
    private FavoriteItemRepository favoriteItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FavoriteItemService favoriteItemService;

    private FavoriteItem favoriteItem;
    private FavoriteItemDto favoriteItemDto;
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

        favoriteItem = FavoriteItem.builder()
                .id(1L)
                .customer(customer)
                .product(product)
                .build();

        favoriteItemDto = FavoriteItemDto.builder()
                .id(1L)
                .userId(1L)
                .productId(1L)
                .build();
    }

    @Test
    void createFavoriteItem_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(favoriteItemRepository.save(any(FavoriteItem.class))).thenReturn(favoriteItem);

        FavoriteItemDto result = favoriteItemService.createFavoriteItem(favoriteItemDto);

        assertNotNull(result);
        assertEquals(favoriteItemDto.getUserId(), result.getUserId());
        assertEquals(favoriteItemDto.getProductId(), result.getProductId());
        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(favoriteItemRepository, times(1)).save(any(FavoriteItem.class));
    }

    @Test
    void createFavoriteItem_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> favoriteItemService.createFavoriteItem(favoriteItemDto));
        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, never()).findById(any());
        verify(favoriteItemRepository, never()).save(any(FavoriteItem.class));
    }

    @Test
    void deleteFavoriteItem_Success() {
        when(favoriteItemRepository.existsById(1L)).thenReturn(true);

        favoriteItemService.deleteFavoriteItem(1L);

        verify(favoriteItemRepository, times(1)).existsById(1L);
        verify(favoriteItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFavoriteItem_NotFound() {
        when(favoriteItemRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> favoriteItemService.deleteFavoriteItem(1L));
        verify(favoriteItemRepository, times(1)).existsById(1L);
        verify(favoriteItemRepository, never()).deleteById(1L);
    }

    @Test
    void getFavoriteItemById_Success() {
        when(favoriteItemRepository.findById(1L)).thenReturn(Optional.of(favoriteItem));

        FavoriteItemDto result = favoriteItemService.getFavoriteItemById(1L);

        assertNotNull(result);
        assertEquals(favoriteItem.getId(), result.getId());
        assertEquals(favoriteItem.getCustomer().getId(), result.getUserId());
        verify(favoriteItemRepository, times(1)).findById(1L);
    }

    @Test
    void getFavoriteItemById_NotFound() {
        when(favoriteItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> favoriteItemService.getFavoriteItemById(1L));
        verify(favoriteItemRepository, times(1)).findById(1L);
    }

    @Test
    void getAllFavoriteItems_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FavoriteItem> page = new PageImpl<>(Arrays.asList(favoriteItem));
        when(favoriteItemRepository.findAll(pageable)).thenReturn(page);

        Page<FavoriteItemDto> result = favoriteItemService.getAllFavoriteItems(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(favoriteItem.getCustomer().getId(), result.getContent().get(0).getUserId());
        verify(favoriteItemRepository, times(1)).findAll(pageable);
    }
}
