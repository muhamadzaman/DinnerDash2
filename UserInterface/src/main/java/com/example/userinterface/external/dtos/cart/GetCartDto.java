package com.example.userinterface.external.dtos.cart;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetCartDto {

    private String id;
    private Long userId;
    private List<CartItem> cartItems;

}
