package com.example.userinterface.external.dtos.cart;

import com.example.userinterface.external.dtos.item.GetItemDto;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItem implements Serializable{

    private Long id;
    private String cartId;
    private Long itemId;
    private Integer quantity;
    private GetItemDto item;

}
