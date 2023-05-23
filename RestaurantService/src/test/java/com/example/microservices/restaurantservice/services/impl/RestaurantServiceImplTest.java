package com.example.microservices.restaurantservice.services.impl;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import com.example.microservices.restaurantservice.entities.Restaurant;
import com.example.microservices.restaurantservice.mappers.DtoMapper;
import com.example.microservices.restaurantservice.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @InjectMocks
    RestaurantServiceImpl restaurantService;

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    DtoMapper mapper;

    Restaurant restaurant1;

    @BeforeEach
    void setup(){
        restaurant1 = new Restaurant(1L, "P3kxh", "RXl");
    }

    @DisplayName("Unit test for get all restaurant method")
    @Test
    void getAllRestaurants() {
        Restaurant restaurant2 = new Restaurant(2L, "vhgc", "wefdfhr");

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));
        List<GetRestaurantDto> restaurantDtos = restaurantService.getAllRestaurants();

        assertNotNull(restaurantDtos);
        assertEquals(restaurantDtos.size(), 2);
    }

    @Test
    void findOne() {
//        given(restaurantRepository.findById(1L)).willReturn(Optional.of(restaurant1));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant1));
        GetRestaurantDto restaurantDto = restaurantService.findOne(1L);

        assertThat(restaurantDto.getName()).isEqualTo(restaurant1.getName());
    }

    @Test
    void createRestaurant() {
    }

    @Test
    void updateRestaurant() {
    }

    @Test
    void updateRestaurantFields() {
    }

    @Test
    void deleteRestaurant() {
    }
}