package com.project.spring.personal.controller.Product;

import com.project.spring.personal.dto.Product.FavoriteItemDto;
import com.project.spring.personal.service.Product.FavoriteItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite-items")
public class FavoriteItemController {

    private final FavoriteItemService favoriteItemService;

    @Autowired
    public FavoriteItemController(FavoriteItemService favoriteItemService) {
        this.favoriteItemService = favoriteItemService;
    }

    @PostMapping
    public ResponseEntity<FavoriteItemDto> createFavoriteItem(@RequestBody FavoriteItemDto favoriteItemDto) {
        FavoriteItemDto createdFavoriteItem = favoriteItemService.createFavoriteItem(favoriteItemDto);
        return ResponseEntity.ok(createdFavoriteItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavoriteItem(@PathVariable Long id) {
        favoriteItemService.deleteFavoriteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteItemDto> getFavoriteItemById(@PathVariable Long id) {
        FavoriteItemDto favoriteItem = favoriteItemService.getFavoriteItemById(id);
        return ResponseEntity.ok(favoriteItem);
    }

    @GetMapping
    public ResponseEntity<Page<FavoriteItemDto>> getAllFavoriteItems(Pageable pageable) {
        Page<FavoriteItemDto> favoriteItems = favoriteItemService.getAllFavoriteItems(pageable);
        return ResponseEntity.ok(favoriteItems);
    }
}