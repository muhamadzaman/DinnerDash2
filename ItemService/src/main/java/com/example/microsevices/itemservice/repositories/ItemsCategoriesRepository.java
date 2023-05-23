package com.example.microsevices.itemservice.repositories;

import com.example.microsevices.itemservice.entities.ItemsCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsCategoriesRepository extends JpaRepository<ItemsCategories, Long> {

    List<ItemsCategories> findAllByCategoryId(Long categoryId);

    List<ItemsCategories> findAllByItemId(Long itemId);
}
