package com.project.spring.personal.controller.Product;

import com.project.spring.personal.dto.Product.DiscountDto;
import com.project.spring.personal.service.Product.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    public ResponseEntity<DiscountDto> createDiscount(@RequestBody DiscountDto discountDto) {
        DiscountDto createdDiscount = discountService.createDiscount(discountDto);
        return ResponseEntity.ok(createdDiscount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountDto> updateDiscount(@PathVariable Long id, @RequestBody DiscountDto discountDto) {
        DiscountDto updatedDiscount = discountService.updateDiscount(id, discountDto);
        return ResponseEntity.ok(updatedDiscount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDto> getDiscountById(@PathVariable Long id) {
        DiscountDto discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(discount);
    }

    @GetMapping
    public ResponseEntity<Page<DiscountDto>> getAllDiscounts(Pageable pageable) {
        Page<DiscountDto> discounts = discountService.getAllDiscounts(pageable);
        return ResponseEntity.ok(discounts);
    }
}