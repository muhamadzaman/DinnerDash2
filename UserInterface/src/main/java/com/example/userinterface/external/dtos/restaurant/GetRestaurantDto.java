package com.example.userinterface.external.dtos.restaurant;

import com.example.userinterface.external.dtos.category.GetCategoryDto;
import com.example.userinterface.external.dtos.item.GetItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantDto {

    private Long id;
    private String name;
    private String address;

    private List<GetCategoryDto> categories = new ArrayList<>();

    private List<GetItemDto> items = new ArrayList<>();

}