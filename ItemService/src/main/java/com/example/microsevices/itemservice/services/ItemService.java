package com.example.microsevices.itemservice.services;

import com.example.centralrepository.dtos.item.GetItemDto;
import com.example.centralrepository.dtos.item.PostItemDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ItemService {

    List<GetItemDto> getAllItems();

    List<GetItemDto> getItemsByRestaurants(Long restaurantId);

    GetItemDto getOneItem(Long itemId);

    GetItemDto saveItem(PostItemDto itemDto, Long restaurantId);

    GetItemDto updateItem(Long itemId, PostItemDto itemDto);

    void deleteItem(Long itemId);

    GetItemDto retireItem(Long itemId);

    GetItemDto allowItem(Long itemId);

    List<GetItemDto> getItemsByCategories(Long categoryId);

    GetItemDto unassignCategory(Long itemId, Long categoryId);

    GetItemDto getFamousItem(Long restaurantId);
}
