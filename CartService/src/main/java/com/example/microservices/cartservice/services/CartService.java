package com.example.microservices.cartservice.services;

import com.example.centralrepository.dtos.cart.PostCartDto;
import com.example.microservices.cartservice.dtos.GetCartDto;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

public interface CartService {

    GetCartDto getCartByUserId(Long userId);

    GetCartDto saveCartFromSession(Long userId, GetCartDto getCartDto);

    void clearUserCart(Long userId);

    GetCartDto incrementQuantity(Long itemId, Long userId);

    GetCartDto decrementQuantity(Long itemId, Long userId);

    GetCartDto saveCartItemByUserId(Long userId, PostCartDto postCartDto);

    GetCartDto removeCartItem(Long userId, Long itemId);
}
