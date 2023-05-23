package com.example.userinterface.external.dtos.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCartDto {

    private Long itemId;
    private Integer quantity = 1;

}
