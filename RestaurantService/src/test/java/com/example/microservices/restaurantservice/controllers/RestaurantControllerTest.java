package com.example.microservices.restaurantservice.controllers;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import com.example.centralrepository.dtos.restaurant.PostRestaurantDto;
import com.example.microservices.restaurantservice.entities.Restaurant;
import com.example.microservices.restaurantservice.services.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @InjectMocks
    RestaurantController restaurantController;

    @Mock
    RestaurantService restaurantService;

    @Test
    void getAll() {
        GetRestaurantDto restaurant1 = new GetRestaurantDto(1L, "P3kxh", "RXl", null, null);
        GetRestaurantDto restaurant2 = new GetRestaurantDto(2L, "vhgc", "wefdfhr", null, null);

        Mockito.when(restaurantService.getAllRestaurants()).thenReturn(Arrays.asList(restaurant1, restaurant2));
        List<GetRestaurantDto> result = restaurantController.getAll().getBody();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo(restaurant1.getName());
        assertThat(result.get(1).getName()).isEqualTo(restaurant2.getName());
    }

    @Test
    void createRestaurant() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Mockito.when(restaurantService.createRestaurant(any(PostRestaurantDto.class)))
                .thenReturn(new GetRestaurantDto(1L, "FRBI", " ubruovbesfovb fvbsje", null, null));
        PostRestaurantDto postRestaurantDto = new PostRestaurantDto(1L, "FRBI", " ubruovbesfovb fvbsje");
        ResponseEntity<GetRestaurantDto> response = restaurantController.createRestaurant(postRestaurantDto);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void getRestaurantById() {
        GetRestaurantDto restaurantDto = new GetRestaurantDto(1L, "GUAWBGUIER", "ewuifbuiewbffo df d", null, null);

        Mockito.when(restaurantService.findOne(any(Long.class))).thenReturn(restaurantDto);
        ResponseEntity<GetRestaurantDto> response = restaurantController.getRestaurantById(restaurantDto.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(restaurantDto.getName());
    }

    @Test
    void deleteRestaurant() {
        Mockito.doNothing().when(restaurantService).deleteRestaurant(any(Long.class));
        restaurantController.deleteRestaurant(1L);

        verify(restaurantService, times(1)).deleteRestaurant(1L);
    }

    @Test
    void updateRestaurant() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Mockito.when(restaurantService.updateRestaurant(any(Long.class), any(PostRestaurantDto.class)))
                .thenReturn(new GetRestaurantDto(1L, "FRBI", " ubruovbesfovb fvbsje", null, null));

        PostRestaurantDto postRestaurantDto = new PostRestaurantDto(1L, "FRBI", " ubruovbesfovb fvbsje");
        ResponseEntity<GetRestaurantDto> response = restaurantController.updateRestaurant(postRestaurantDto.getId() ,postRestaurantDto);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(postRestaurantDto.getName());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateRestaurantFields() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Mockito.when(restaurantService.updateRestaurantFields(any(Map.class) ,any(Long.class)))
                .thenReturn(new GetRestaurantDto(1L, "FRBI", " ubruovbesfovb fvbsje", null, null));

        Map<String, Object> restaurant = new HashMap<>();
        restaurant.put("name", "FRBI");
        ResponseEntity<GetRestaurantDto> response = restaurantController.updateRestaurantFields(1L, restaurant);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getName(), restaurant.get("name"));
    }
}