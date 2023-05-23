package com.example.microsevices.itemservice.external.services;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RESTAURANT-SERVICE")
public interface RestaurantService {

    @GetMapping("/restaurants/{restaurantId}")
    GetRestaurantDto getRestaurantById(@PathVariable Long restaurantId);

}
