package com.example.microservices.restaurantservice.mappers;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import com.example.centralrepository.dtos.restaurant.PostRestaurantDto;
import com.example.microservices.restaurantservice.entities.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GetRestaurantDto covertToDto(Restaurant restaurant){
        return modelMapper.map(restaurant, GetRestaurantDto.class);
    }

    public Restaurant convertDtoToRestaurant(PostRestaurantDto restaurantDto){
        return modelMapper.map(restaurantDto, Restaurant.class);
    }

    public PostRestaurantDto convertGetToPostRestaurantDto(GetRestaurantDto restaurantDto){
        return modelMapper.map(restaurantDto, PostRestaurantDto.class);
    }
}
