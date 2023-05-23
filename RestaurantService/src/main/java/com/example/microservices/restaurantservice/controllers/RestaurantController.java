package com.example.microservices.restaurantservice.controllers;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import com.example.centralrepository.dtos.restaurant.PostRestaurantDto;
import com.example.microservices.restaurantservice.services.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<GetRestaurantDto>> getAll(){
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @PostMapping
    public ResponseEntity<GetRestaurantDto> createRestaurant(@Valid @RequestBody PostRestaurantDto restaurantDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(restaurantDto));
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<GetRestaurantDto> getRestaurantById(@PathVariable Long restaurantId){
        return ResponseEntity.ok(restaurantService.findOne(restaurantId));
    }

    @DeleteMapping("/{restaurantId}")
    public void deleteRestaurant(@PathVariable Long restaurantId){
        restaurantService.deleteRestaurant(restaurantId);
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<GetRestaurantDto> updateRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody PostRestaurantDto restaurantDto){
        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantId, restaurantDto));
    }

    @PatchMapping("/{restaurantId}")
    public ResponseEntity<GetRestaurantDto> updateRestaurantFields(
            @PathVariable Long restaurantId,
            @RequestBody Map<String, Object> restaurant){
        return ResponseEntity.ok(restaurantService.updateRestaurantFields(restaurant ,restaurantId));
    }
}
