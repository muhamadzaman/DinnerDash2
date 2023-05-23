package com.example.microservices.categoryservice.mappers;

import com.example.centralrepository.dtos.category.GetCategoryDto;
import com.example.centralrepository.dtos.category.PostCategoryDto;
import com.example.microservices.categoryservice.entities.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ModelMapper modelMapper;


    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GetCategoryDto convertToDto(Category category){
        return modelMapper.map(category, GetCategoryDto.class);
    }

    public Category convertDtoToCategory(PostCategoryDto categoryDto){
        return modelMapper.map(categoryDto, Category.class);
    }
}
