package com.example.microservices.categoryservice.repositories;

import com.example.microservices.categoryservice.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByRestaurantId(Long restaurantId);

}
