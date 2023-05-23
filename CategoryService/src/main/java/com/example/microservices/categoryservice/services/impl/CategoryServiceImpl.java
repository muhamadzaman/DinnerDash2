package com.example.microservices.categoryservice.services.impl;

import com.example.centralrepository.dtos.category.GetCategoryDto;
import com.example.centralrepository.dtos.category.PostCategoryDto;
import com.example.microservices.categoryservice.entities.Category;
import com.example.microservices.categoryservice.mappers.DtoMapper;
import com.example.microservices.categoryservice.repositories.CategoryRepository;
import com.example.microservices.categoryservice.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final DtoMapper dtoMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, DtoMapper dtoMapper) {
        this.categoryRepository = categoryRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public List<GetCategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(dtoMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public GetCategoryDto getOneCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).map(dtoMapper::convertToDto).orElseThrow(
                () -> new NoSuchElementException("Unable to find resource with id " + categoryId)
        );
    }

    @Override
    public GetCategoryDto saveCategory(PostCategoryDto categoryDto) {
        Category category = dtoMapper.convertDtoToCategory(categoryDto);
        return dtoMapper.convertToDto(categoryRepository.save(category));
    }

    @Override
    public GetCategoryDto updateCategory(Long categoryId, PostCategoryDto categoryDto) {
        categoryDto.setId(categoryId);
        return saveCategory(categoryDto);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        GetCategoryDto category = getOneCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<GetCategoryDto> getAllByRestaurantId(Long restaurantId) {
        return categoryRepository.findAllByRestaurantId(restaurantId).stream().map(dtoMapper::convertToDto).toList();
    }

}
