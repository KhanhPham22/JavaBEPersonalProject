package com.project.spring.personal.service.Product;

import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.entity.product.Rating;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.repository.product.RatingRepository;
import com.project.spring.personal.dto.Product.RatingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository,
                         CustomerRepository customerRepository,
                         ProductRepository productRepository) {
        this.ratingRepository = ratingRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public RatingDto createRating(RatingDto ratingDto) {
        if (ratingDto.getScore() < 1 || ratingDto.getScore() > 5) {
            throw new IllegalArgumentException("Rating score must be between 1 and 5");
        }

        Customer customer = customerRepository.findById(ratingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + ratingDto.getUserId()));
        Product product = productRepository.findById(ratingDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + ratingDto.getProductId()));

        Rating rating = Rating.builder()
                .stars(ratingDto.getScore())
                .comment(ratingDto.getComment())
                .customer(customer)
                .product(product)
                .build();

        Rating savedRating = ratingRepository.save(rating);
        return convertToDto(savedRating);
    }

    public RatingDto updateRating(Long id, RatingDto ratingDto) {
        if (ratingDto.getScore() < 1 || ratingDto.getScore() > 5) {
            throw new IllegalArgumentException("Rating score must be between 1 and 5");
        }

        Optional<Rating> optionalRating = ratingRepository.findById(id);
        if (optionalRating.isPresent()) {
            Rating rating = optionalRating.get();
            Customer customer = customerRepository.findById(ratingDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + ratingDto.getUserId()));
            Product product = productRepository.findById(ratingDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + ratingDto.getProductId()));

            rating.setStars(ratingDto.getScore());
            rating.setComment(ratingDto.getComment());
            rating.setCustomer(customer);
            rating.setProduct(product);

            Rating updatedRating = ratingRepository.save(rating);
            return convertToDto(updatedRating);
        }
        throw new RuntimeException("Rating not found with id: " + id);
    }

    public void deleteRating(Long id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Rating not found with id: " + id);
        }
    }

    public RatingDto getRatingById(Long id) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        if (optionalRating.isPresent()) {
            return convertToDto(optionalRating.get());
        }
        throw new RuntimeException("Rating not found with id: " + id);
    }

    public Page<RatingDto> getAllRatings(Pageable pageable) {
        return ratingRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    private RatingDto convertToDto(Rating rating) {
        return RatingDto.builder()
                .id(rating.getId())
                .score(rating.getStars())
                .comment(rating.getComment())
                .productId(rating.getProduct().getId())
                .userId(rating.getCustomer().getId())
                .build();
    }
}