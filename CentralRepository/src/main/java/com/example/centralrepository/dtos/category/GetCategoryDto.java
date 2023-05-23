package com.example.centralrepository.dtos.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GetCategoryDto {

    private Long id;
    private String name;
    private Long restaurantId;

}
