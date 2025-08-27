package com.project.spring.personal.service.Product;

import com.project.spring.personal.entity.product.Discount;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.repository.product.DiscountRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.dto.Product.DiscountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository, ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    public DiscountDto createDiscount(DiscountDto discountDto) {
        Product product = productRepository.findById(discountDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + discountDto.getProductId()));

        Discount discount = Discount.builder()
                .code(discountDto.getCode())
                .percentage(BigDecimal.valueOf(discountDto.getPercentage()))
                .description(discountDto.getDescription())
                .product(product)
                .active(true)
                .build();

        Discount savedDiscount = discountRepository.save(discount);
        return convertToDto(savedDiscount);
    }

    public DiscountDto updateDiscount(Long id, DiscountDto discountDto) {
        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        if (optionalDiscount.isPresent()) {
            Discount discount = optionalDiscount.get();
            Product product = productRepository.findById(discountDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + discountDto.getProductId()));

            discount.setCode(discountDto.getCode());
            discount.setPercentage(BigDecimal.valueOf(discountDto.getPercentage()));
            discount.setDescription(discountDto.getDescription());
            discount.setProduct(product);

            Discount updatedDiscount = discountRepository.save(discount);
            return convertToDto(updatedDiscount);
        }
        throw new RuntimeException("Discount not found with id: " + id);
    }

    public void deleteDiscount(Long id) {
        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
        } else {
            throw new RuntimeException("Discount not found with id: " + id);
        }
    }

    public DiscountDto getDiscountById(Long id) {
        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        if (optionalDiscount.isPresent()) {
            return convertToDto(optionalDiscount.get());
        }
        throw new RuntimeException("Discount not found with id: " + id);
    }

    public Page<DiscountDto> getAllDiscounts(Pageable pageable) {
        return discountRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    private DiscountDto convertToDto(Discount discount) {
        return DiscountDto.builder()
                .id(discount.getId())
                .code(discount.getCode())
                .percentage(discount.getPercentage().doubleValue())
                .description(discount.getDescription())
                .productId(discount.getProduct().getId())
                .build();
    }
}