package com.example.centralrepository.dtos.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class PostCategoryDto {

    private Long id;

    @NotBlank(message = "Category name must exist!")
    private String name;

    @NotNull(message = "Restaurant id must exists")
    private Long restaurantId;

}
