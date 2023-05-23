package com.example.microservices.restaurantservice.services;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import com.example.centralrepository.dtos.restaurant.PostRestaurantDto;

import java.util.List;
import java.util.Map;

public interface RestaurantService {

    List<GetRestaurantDto> getAllRestaurants();

    GetRestaurantDto findOne(Long restaurantId);

    GetRestaurantDto createRestaurant(PostRestaurantDto restaurantDto);

    GetRestaurantDto updateRestaurant(Long restaurantId, PostRestaurantDto restaurantDto);

    GetRestaurantDto updateRestaurantFields(Map<String, Object> fields, Long restaurantId);

    void deleteRestaurant(Long restaurantId);
}
