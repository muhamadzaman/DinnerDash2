package com.example.microsevices.itemservice.repositories;

import com.example.microsevices.itemservice.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByRestaurantId(Long restaurantId);

    Optional<Item> findByTitle(String title);
}
