package com.project.spring.personal.service.Product;

import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.product.FavoriteItem;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.product.FavoriteItemRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.dto.Product.FavoriteItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteItemService {

    private final FavoriteItemRepository favoriteItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public FavoriteItemService(FavoriteItemRepository favoriteItemRepository,
                               CustomerRepository customerRepository,
                               ProductRepository productRepository) {
        this.favoriteItemRepository = favoriteItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public FavoriteItemDto createFavoriteItem(FavoriteItemDto favoriteItemDto) {
        Customer customer = customerRepository.findById(favoriteItemDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + favoriteItemDto.getUserId()));
        Product product = productRepository.findById(favoriteItemDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + favoriteItemDto.getProductId()));

        FavoriteItem favoriteItem = FavoriteItem.builder()
                .customer(customer)
                .product(product)
                .build();

        FavoriteItem savedFavoriteItem = favoriteItemRepository.save(favoriteItem);
        return convertToDto(savedFavoriteItem);
    }

    public void deleteFavoriteItem(Long id) {
        if (favoriteItemRepository.existsById(id)) {
            favoriteItemRepository.deleteById(id);
        } else {
            throw new RuntimeException("FavoriteItem not found with id: " + id);
        }
    }

    public FavoriteItemDto getFavoriteItemById(Long id) {
        Optional<FavoriteItem> optionalFavoriteItem = favoriteItemRepository.findById(id);
        if (optionalFavoriteItem.isPresent()) {
            return convertToDto(optionalFavoriteItem.get());
        }
        throw new RuntimeException("FavoriteItem not found with id: " + id);
    }

    public Page<FavoriteItemDto> getAllFavoriteItems(Pageable pageable) {
        return favoriteItemRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    private FavoriteItemDto convertToDto(FavoriteItem favoriteItem) {
        return FavoriteItemDto.builder()
                .id(favoriteItem.getId())
                .userId(favoriteItem.getCustomer().getId())
                .productId(favoriteItem.getProduct().getId())
                .build();
    }
}