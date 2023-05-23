package com.example.microservices.restaurantservice.services;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import com.example.centralrepository.dtos.restaurant.PostRestaurantDto;
import com.example.microservices.restaurantservice.entities.Restaurant;
import com.example.microservices.restaurantservice.external.services.CategoryService;
import com.example.microservices.restaurantservice.external.services.ItemService;
import com.example.microservices.restaurantservice.mappers.DtoMapper;
import com.example.microservices.restaurantservice.repositories.RestaurantRepository;
import com.example.microservices.restaurantservice.services.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    RestaurantServiceImpl restaurantService;

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    DtoMapper dtoMapper;

    @Mock
    ItemService itemService;

    @Mock
    CategoryService categoryService;

    GetRestaurantDto restaurantDto1;

    @BeforeEach
    void setup(){
        restaurantDto1 = new GetRestaurantDto(1L, "P3kxh", "RXl", null, null);
    }

    @Test
    void getAllRestaurants() {
        Restaurant restaurant1 = new Restaurant(1L, "P3kxh", "RXl");
        Restaurant restaurant2 = new Restaurant(2L, "vhgc", "wefdfhr");

        GetRestaurantDto restaurantDto2 = new GetRestaurantDto(2L, "vhgc", "wefdfhr", null, null);

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));
        when(dtoMapper.covertToDto(restaurant1)).thenReturn(restaurantDto1);
        when(dtoMapper.covertToDto(restaurant2)).thenReturn(restaurantDto2);
        List<GetRestaurantDto> restaurantDtos = restaurantService.getAllRestaurants();

        assertNotNull(restaurantDtos);
        assertEquals(restaurantDtos.size(), 2);
        assertEquals(restaurantDtos.get(0).getName(), restaurant1.getName());
        assertEquals(restaurantDtos.get(1).getName(), restaurant2.getName());
    }

    @Test
    void findOne() {
        Restaurant restaurant1 = new Restaurant(1L, "P3kxh", "RXl");

        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant1));
        when(dtoMapper.covertToDto(any(Restaurant.class))).thenReturn(restaurantDto1);
        GetRestaurantDto restaurantDto = restaurantService.findOne(1L);

        assertNotNull(restaurantDto);
        assertEquals(restaurantDto, restaurantDto1);
        assertThat(restaurantDto.getName()).isEqualTo(restaurant1.getName());
    }

    @Test
    void createRestaurant() {
        PostRestaurantDto postRestaurantDto = new PostRestaurantDto(1L, "P3kxh", "RXl");
        Restaurant restaurant1 = new Restaurant(1L, "P3kxh", "RXl");

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant1);
        when(dtoMapper.convertDtoToRestaurant(postRestaurantDto)).thenReturn(restaurant1);
        when(dtoMapper.covertToDto(restaurant1)).thenReturn(restaurantDto1);
        GetRestaurantDto restaurantDto = restaurantService.createRestaurant(postRestaurantDto);

        assertNotNull(restaurantDto);
        assertEquals(restaurantDto.getName(), postRestaurantDto.getName());
    }

    @Test
    void updateRestaurant() {
        PostRestaurantDto postRestaurantDto = new PostRestaurantDto(1L, "P3kxh", "RXl");
        Restaurant restaurant = new Restaurant(1L, "P3kxh", "RXl");

        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(dtoMapper.covertToDto(any(Restaurant.class))).thenReturn(restaurantDto1);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(dtoMapper.convertDtoToRestaurant(postRestaurantDto)).thenReturn(restaurant);
        when(dtoMapper.covertToDto(restaurant)).thenReturn(restaurantDto1);

        GetRestaurantDto getRestaurantDto = restaurantService.updateRestaurant(restaurant.getId(), postRestaurantDto);

        assertNotNull(getRestaurantDto);
        assertEquals(getRestaurantDto.getName(), restaurant.getName());
    }

    @Test
    void updateRestaurantFields() {
    }

    @Test
    void deleteRestaurant() {
    }
}