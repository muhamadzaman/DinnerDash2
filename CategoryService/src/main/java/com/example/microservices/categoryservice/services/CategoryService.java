package com.example.microservices.categoryservice.services;

import com.example.centralrepository.dtos.category.GetCategoryDto;
import com.example.centralrepository.dtos.category.PostCategoryDto;

import java.util.List;

public interface CategoryService {

    List<GetCategoryDto> getAllCategories();

    GetCategoryDto getOneCategory(Long categoryId);

    GetCategoryDto saveCategory(PostCategoryDto categoryDto);

    GetCategoryDto updateCategory(Long categoryId, PostCategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    List<GetCategoryDto> getAllByRestaurantId(Long restaurantId);
}
