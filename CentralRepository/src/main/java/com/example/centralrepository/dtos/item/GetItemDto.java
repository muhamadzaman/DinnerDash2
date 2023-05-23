package com.example.centralrepository.dtos.item;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetItemDto {

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private Long restaurantId;

    private Boolean retired;

    private List<Long> categoryIds;

    private GetRestaurantDto itemRestaurant;

    private String imageUrl;
}
