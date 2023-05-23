package com.example.centralrepository.dtos.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCartDto {

    @NotNull(message = "Item must exist!")
    private Long itemId;

    @Min(message = "Atleast one item should be added in cart", value = 1)
    private Integer quantity;

}
