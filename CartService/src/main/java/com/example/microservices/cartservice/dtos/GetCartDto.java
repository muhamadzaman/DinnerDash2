package com.example.microservices.cartservice.dtos;

import com.example.microservices.cartservice.entities.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCartDto {

    private String id;
    private Long userId;
    private List<CartItem> cartItems;

}
