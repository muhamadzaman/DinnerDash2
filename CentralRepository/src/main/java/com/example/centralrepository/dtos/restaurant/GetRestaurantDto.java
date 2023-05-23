package com.example.centralrepository.dtos.restaurant;

import com.example.centralrepository.dtos.category.GetCategoryDto;
import com.example.centralrepository.dtos.item.GetItemDto;
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

    private List<GetItemDto> items = new ArrayList<>();

    private List<GetCategoryDto> categories = new ArrayList<>();
}
