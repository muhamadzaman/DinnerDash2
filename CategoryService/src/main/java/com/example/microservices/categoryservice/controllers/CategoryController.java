package com.example.microservices.categoryservice.controllers;

import com.example.centralrepository.dtos.category.GetCategoryDto;
import com.example.centralrepository.dtos.category.PostCategoryDto;
import com.example.microservices.categoryservice.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<GetCategoryDto>> getAll(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<GetCategoryDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody PostCategoryDto categoryDto){
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDto));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<GetCategoryDto> getOne(@PathVariable Long categoryId){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getOneCategory(categoryId));
    }

    @PostMapping
    public ResponseEntity<GetCategoryDto> saveCategory(@Valid @RequestBody PostCategoryDto categoryDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<GetCategoryDto>> findAllByRestaurantId(@PathVariable Long restaurantId){
        return ResponseEntity.ok(categoryService.getAllByRestaurantId(restaurantId));
    }

}
