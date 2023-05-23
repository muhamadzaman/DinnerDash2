package com.example.microservices.cartservice.mappers;

import com.example.microservices.cartservice.dtos.GetCartDto;
import com.example.microservices.cartservice.entities.Cart;
import com.example.microservices.cartservice.entities.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoMapper {

    public GetCartDto mapToGetDto(Cart cart, List<CartItem> cartItems){
        GetCartDto getCartDto = new GetCartDto();
        getCartDto.setId(cart.getId());
        getCartDto.setUserId(cart.getUserId());
        getCartDto.setCartItems(cartItems);
        return getCartDto;
    }

}
