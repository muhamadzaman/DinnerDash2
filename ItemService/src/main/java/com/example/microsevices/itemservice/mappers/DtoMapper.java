package com.example.microsevices.itemservice.mappers;

import com.example.centralrepository.dtos.item.GetItemDto;
import com.example.centralrepository.dtos.item.PostItemDto;
import com.example.microsevices.itemservice.entities.Item;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GetItemDto convertItemToDto(Item item){
        return modelMapper.map(item, GetItemDto.class);
    }

    public Item convertDtoToItem(PostItemDto itemDto){
        return modelMapper.map(itemDto, Item.class);
    }

    public PostItemDto convertGetToPostDto(GetItemDto itemDto){
        return modelMapper.map(itemDto, PostItemDto.class);
    }

    public Item convertGetToItem(GetItemDto itemDto){ return modelMapper.map(itemDto, Item.class); }
}
