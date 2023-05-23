package com.example.microservices.restaurantservice.repositories;

import com.example.microservices.restaurantservice.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
