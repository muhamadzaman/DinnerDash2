package com.example.microservices.restaurantservice.services.impl;

import com.example.centralrepository.dtos.category.GetCategoryDto;
import com.example.centralrepository.dtos.item.GetItemDto;
import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import com.example.centralrepository.dtos.restaurant.PostRestaurantDto;
import com.example.microservices.restaurantservice.entities.Restaurant;
import com.example.microservices.restaurantservice.external.services.CategoryService;
import com.example.microservices.restaurantservice.external.services.ItemService;
import com.example.microservices.restaurantservice.mappers.DtoMapper;
import com.example.microservices.restaurantservice.repositories.RestaurantRepository;
import com.example.microservices.restaurantservice.services.RestaurantService;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final DtoMapper dtoMapper;

    private final ItemService itemService;

    private final CategoryService categoryService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, DtoMapper dtoMapper, ItemService itemService, CategoryService categoryService) {
        this.restaurantRepository = restaurantRepository;
        this.dtoMapper = dtoMapper;
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @Override
    public List<GetRestaurantDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream().map(
                dtoMapper::covertToDto
        ).collect(Collectors.toList());
    }

    @Override
    public GetRestaurantDto findOne(Long restaurantId) {
        GetRestaurantDto restaurantDto = restaurantRepository.findById(restaurantId).map(dtoMapper::covertToDto).orElseThrow(
                () -> new NoSuchElementException("Unable to find resource with id " + restaurantId + " on server!")
        );
        List<GetItemDto> itemDtos = itemService.getItemsByRestaurantId(restaurantDto.getId());
        restaurantDto.setItems(itemDtos);
        List<GetCategoryDto> categoryDtos = categoryService.findAllByRestaurantId(restaurantDto.getId());
        restaurantDto.setCategories(categoryDtos);
        return restaurantDto;
    }

    @Override
    public GetRestaurantDto createRestaurant(PostRestaurantDto restaurantDto) {
        return dtoMapper.covertToDto(restaurantRepository.save(dtoMapper.convertDtoToRestaurant(restaurantDto)));
    }

    @Override
    public GetRestaurantDto updateRestaurant(Long restaurantId, PostRestaurantDto restaurantDto) {
        GetRestaurantDto restaurantDto1 = findOne(restaurantId);
        restaurantDto.setId(restaurantId);
        return createRestaurant(restaurantDto);
    }

    @Override
    public GetRestaurantDto updateRestaurantFields(Map<String, Object> fields, Long restaurantId) {
        GetRestaurantDto restaurantDto = findOne(restaurantId);
        fields.forEach((key,value) -> {
            Field field = ReflectionUtils.findField(GetRestaurantDto.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, restaurantDto, value);
        });
        return createRestaurant(dtoMapper.convertGetToPostRestaurantDto(restaurantDto));
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        findOne(restaurantId);
        restaurantRepository.deleteById(restaurantId);
    }
}
