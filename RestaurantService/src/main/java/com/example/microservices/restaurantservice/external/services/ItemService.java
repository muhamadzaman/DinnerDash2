package com.example.microservices.restaurantservice.external.services;

import com.example.centralrepository.dtos.item.GetItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ITEM-SERVICE")
public interface ItemService {

    @GetMapping("/items/restaurants/{restaurantId}")
    List<GetItemDto> getItemsByRestaurantId(@PathVariable Long restaurantId);

}
