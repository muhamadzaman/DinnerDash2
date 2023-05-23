package com.example.userinterface.external.dtos.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRestaurantDto {

    private Long id;
    private String name;
    private String address;

}
