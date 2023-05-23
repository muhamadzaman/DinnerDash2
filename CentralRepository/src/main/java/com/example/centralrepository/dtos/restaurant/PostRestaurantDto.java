package com.example.centralrepository.dtos.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRestaurantDto {

    private Long id;

    @NotBlank(message = "Restaurant name should exists!")
    private String name;

    private String address;

}
