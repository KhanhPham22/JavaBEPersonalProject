package com.project.spring.personal.controller.Product;

import com.project.spring.personal.dto.Product.RatingDto;
import com.project.spring.personal.service.Product.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<RatingDto> createRating(@RequestBody RatingDto ratingDto) {
        RatingDto createdRating = ratingService.createRating(ratingDto);
        return ResponseEntity.ok(createdRating);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RatingDto> updateRating(@PathVariable Long id, @RequestBody RatingDto ratingDto) {
        RatingDto updatedRating = ratingService.updateRating(id, ratingDto);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingDto> getRatingById(@PathVariable Long id) {
        RatingDto rating = ratingService.getRatingById(id);
        return ResponseEntity.ok(rating);
    }

    @GetMapping
    public ResponseEntity<Page<RatingDto>> getAllRatings(Pageable pageable) {
        Page<RatingDto> ratings = ratingService.getAllRatings(pageable);
        return ResponseEntity.ok(ratings);
    }
}