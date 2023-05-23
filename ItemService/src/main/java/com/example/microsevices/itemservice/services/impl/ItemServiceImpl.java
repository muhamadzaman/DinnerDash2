package com.example.microsevices.itemservice.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.centralrepository.dtos.item.GetItemDto;
import com.example.centralrepository.dtos.item.OrderItems;
import com.example.centralrepository.dtos.item.PostItemDto;
import com.example.microsevices.itemservice.entities.Item;
import com.example.microsevices.itemservice.entities.ItemsCategories;
import com.example.microsevices.itemservice.external.services.OrderService;
import com.example.microsevices.itemservice.external.services.RestaurantService;
import com.example.microsevices.itemservice.mappers.DtoMapper;
import com.example.microsevices.itemservice.repositories.ItemRepository;
import com.example.microsevices.itemservice.repositories.ItemsCategoriesRepository;
import com.example.microsevices.itemservice.services.ItemService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ItemsCategoriesRepository categoriesRepository;

    private final OrderService orderService;

    private final DtoMapper dtoMapper;

    private final Cloudinary cloudinary;

    private final RestaurantService restaurantService;

    public ItemServiceImpl(
            ItemRepository itemRepository,
            ItemsCategoriesRepository categoriesRepository,
            OrderService orderService, DtoMapper dtoMapper,
            Cloudinary cloudinary, RestaurantService restaurantService) {
        this.itemRepository = itemRepository;
        this.categoriesRepository = categoriesRepository;
        this.orderService = orderService;
        this.dtoMapper = dtoMapper;
        this.cloudinary = cloudinary;
        this.restaurantService = restaurantService;
    }

    @Override
    public List<GetItemDto> getAllItems() {
        List<GetItemDto> itemDtos = itemRepository.findAll().stream().map(dtoMapper::convertItemToDto).collect(Collectors.toList());
        itemDtos.forEach(
                this::setCategories
        );
        return itemDtos;
    }

    @Override
    public List<GetItemDto> getItemsByRestaurants(Long restaurantId) {
        List<GetItemDto> itemDtos = itemRepository
                .findAllByRestaurantId(restaurantId)
                .stream()
                .map(dtoMapper::convertItemToDto)
                .collect(Collectors.toList());
        itemDtos.forEach(
                this::setCategories
        );
        return itemDtos;
    }

    @Override
    public GetItemDto getOneItem(Long itemId) {
        GetItemDto itemDto = itemRepository.findById(itemId).map(dtoMapper::convertItemToDto).orElseThrow(
                () -> new NoSuchElementException("Unable to find resource")
        );
//        GetRestaurantDto restaurantDto = restaurantService.getRestaurantById(itemDto.getRestaurantId());
//        itemDto.setItemRestaurant(restaurantDto);
        setCategories(itemDto);
        return itemDto;
    }

    @Override
    public GetItemDto saveItem(PostItemDto itemDto, Long restaurantId) {
        checkTitle(itemDto);
        if (itemDto.getPrice().precision() > 8 || itemDto.getPrice().scale() > 2){
            throw new IllegalArgumentException("Price precision should be 8 and scale should be 2");
        }
        itemDto.setRestaurantId(restaurantId);
        Item entity = dtoMapper.convertDtoToItem(itemDto);
        if (itemDto.getFile() != null && !itemDto.getFile().isEmpty()){
            try {
                Map uploadResult = cloudinary.uploader().upload(itemDto.getFile().getBytes(), ObjectUtils.emptyMap());
                entity.setImageUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Item savedItem = itemRepository.save(entity);
        GetItemDto itemDto1 = dtoMapper.convertItemToDto(savedItem);
        itemDto1.setCategoryIds(saveCategories(itemDto, savedItem));
        return itemDto1;
    }

    @Override
    public GetItemDto updateItem(Long itemId, PostItemDto itemDto) {
        GetItemDto itemDto1 = getOneItem(itemId);
        itemDto.setId(itemId);
        itemDto.setRestaurantId(itemDto1.getRestaurantId());
        clearItemsCategories(itemId);
        return saveItem(itemDto, itemDto.getRestaurantId());
    }

    @Override
    public void deleteItem(Long itemId) {
        GetItemDto itemDto = getOneItem(itemId);
        itemRepository.deleteById(itemDto.getId());
    }

    @Override
    public GetItemDto retireItem(Long itemId) {
        Item item = dtoMapper.convertGetToItem(getOneItem(itemId));
        item.setRetired(true);
        return setCategories(dtoMapper.convertItemToDto(itemRepository.save(item)));
    }

    @Override
    public GetItemDto allowItem(Long itemId) {
        Item item = dtoMapper.convertGetToItem(getOneItem(itemId));
        item.setRetired(false);
        return setCategories(dtoMapper.convertItemToDto(itemRepository.save(item)));
    }

    @Override
    public List<GetItemDto> getItemsByCategories(Long categoryId){
        List<ItemsCategories> itemsCategories = categoriesRepository.findAllByCategoryId(categoryId);
        List<GetItemDto> itemDtos = new ArrayList<>();
        itemsCategories.forEach(
                cat -> itemDtos.add(getOneItem(cat.getItemId()))
        );
        return itemDtos;
    }

    private List<Long> saveCategories(PostItemDto itemDto, Item savedItem){
        return itemDto.getCategoryIds().stream().map(
                id -> {
                    ItemsCategories itemsCategories = new ItemsCategories();
                    itemsCategories.setCategoryId(id);
                    itemsCategories.setItemId(savedItem.getId());
                    return categoriesRepository.save(itemsCategories).getCategoryId();
                }
        ).toList();
    }

    @Override
    public GetItemDto unassignCategory(Long itemId, Long categoryId) {
        GetItemDto itemDto = getOneItem(itemId);
        Optional<ItemsCategories> itemsCategories = categoriesRepository.findAllByCategoryId(categoryId).stream().filter(
                itemsCategory -> itemsCategory.getItemId().equals(itemId)
        ).findFirst();
        if (itemsCategories.isEmpty()){
            return itemDto;
        }
        categoriesRepository.delete(itemsCategories.get());
        return setCategories(itemDto);
    }

    private void checkTitle(PostItemDto itemDto){
        Optional<Item> itemOptional = itemRepository.findByTitle(itemDto.getTitle());
        if(itemOptional.isPresent()) {
            if (Objects.equals(itemDto.getId(), itemOptional.get().getId())){
                return;
            }
            throw new IllegalArgumentException("Item with title " + itemDto.getTitle() + " already exists!");
        }
    }

    private void clearItemsCategories(Long itemId){
        categoriesRepository.findAllByItemId(itemId).forEach(
                cat -> categoriesRepository.deleteById(cat.getId())
        );
    }

    private GetItemDto setCategories(GetItemDto itemDto){
        itemDto.setCategoryIds(
                categoriesRepository
                        .findAllByItemId(itemDto.getId())
                        .stream().map(ItemsCategories::getCategoryId)
                        .toList()
        );
        return itemDto;
    }

    @Override
    public GetItemDto getFamousItem(Long restaurantId) {
        List<Item> items = itemRepository.findAllByRestaurantId(restaurantId);
        Map<Item, Integer> ordersCount = new HashMap<>();
        items.forEach(
                item -> {
                    List<OrderItems> orderItems = orderService.getAllOrdersByItemId(item.getId());
                    ordersCount.put(item, orderItems.size());
                }
        );
        Item maxCountItem = Collections.max(ordersCount.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        return dtoMapper.convertItemToDto(maxCountItem);
    }
}
