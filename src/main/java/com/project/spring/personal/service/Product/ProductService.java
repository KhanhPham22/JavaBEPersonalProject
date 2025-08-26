package com.project.spring.personal.service.Product;

import com.project.spring.personal.entity.product.Category;
import com.project.spring.personal.entity.product.Product;
import com.project.spring.personal.repository.product.CategoryRepository;
import com.project.spring.personal.repository.product.ProductRepository;
import com.project.spring.personal.dto.Product.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDto createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));

        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(BigDecimal.valueOf(productDto.getPrice()))
                .stockQuantity(productDto.getStock())
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(BigDecimal.valueOf(productDto.getPrice()));
            product.setStockQuantity(productDto.getStock());
            product.setCategory(category);

            Product updatedProduct = productRepository.save(product);
            return convertToDto(updatedProduct);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public ProductDto getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return convertToDto(optionalProduct.get());
        }
        throw new RuntimeException("Product not found with id: " + id);
    }

    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    private ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice().doubleValue())
                .stock(product.getStockQuantity())
                .categoryId(product.getCategory().getId())
                .build();
    }
}