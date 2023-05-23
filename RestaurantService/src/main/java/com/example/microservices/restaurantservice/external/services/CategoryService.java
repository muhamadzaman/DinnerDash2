package com.example.microservices.restaurantservice.external.services;

import com.example.centralrepository.dtos.category.GetCategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CATEGORY-SERVICE")
public interface CategoryService {

    @GetMapping("/categories/restaurants/{restaurantId}")
    List<GetCategoryDto> findAllByRestaurantId(@PathVariable Long restaurantId);

}
