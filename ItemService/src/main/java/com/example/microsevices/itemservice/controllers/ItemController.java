package com.example.microsevices.itemservice.controllers;

import com.example.centralrepository.dtos.item.GetItemDto;
import com.example.centralrepository.dtos.item.PostItemDto;
import com.example.microsevices.itemservice.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<GetItemDto>> getAllItems(){
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping(path = "/restaurants/{restaurantId}")
    public ResponseEntity<List<GetItemDto>> getItemsByRestaurantId(@PathVariable Long restaurantId){
        return ResponseEntity.ok(itemService.getItemsByRestaurants(restaurantId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<GetItemDto> getOneItem(@PathVariable Long itemId){
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getOneItem(itemId));
    }

    @PostMapping( value = "/restaurants/{restaurantId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GetItemDto> saveItem(@Valid PostItemDto itemDto,
                                               @PathVariable Long restaurantId){
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.saveItem(itemDto, restaurantId));
    }

    @PutMapping(value = "/{itemId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GetItemDto> updateItem(
            @PathVariable Long itemId,
            @Valid PostItemDto itemDto
    ){
        return ResponseEntity.ok(itemService.updateItem(itemId, itemDto));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId){
        itemService.deleteItem(itemId);
    }

    @PutMapping("/{itemId}/retire")
    public ResponseEntity<GetItemDto> retireItem(@PathVariable Long itemId){
        return ResponseEntity.ok(itemService.retireItem(itemId));
    }

    @PutMapping("/{itemId}/allow")
    public ResponseEntity<GetItemDto> allowItm(@PathVariable Long itemId){
        return ResponseEntity.ok(itemService.allowItem(itemId));
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<GetItemDto>> getItemsByCategories(@PathVariable Long categoryId){
        return ResponseEntity.ok(itemService.getItemsByCategories(categoryId));
    }

    @PutMapping("/{itemId}/unassign")
    public ResponseEntity<GetItemDto> unassignCategory(@PathVariable Long itemId, @RequestBody Long categoryId){
        return ResponseEntity.ok(itemService.unassignCategory(itemId, categoryId));
    }

    @GetMapping("/restaurants/{restaurantId}/famous")
    public ResponseEntity<GetItemDto> getFamousItem(@PathVariable Long restaurantId){
        return ResponseEntity.ok(itemService.getFamousItem(restaurantId));
    }
}
