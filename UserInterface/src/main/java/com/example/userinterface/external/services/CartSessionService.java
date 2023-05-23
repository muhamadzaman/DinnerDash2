package com.example.userinterface.external.services;

import com.example.userinterface.external.dtos.cart.GetCartDto;
import com.example.userinterface.external.dtos.cart.PostCartDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface CartSessionService {

    GetCartDto getCartFromSession(HttpSession httpSession);

    GetCartDto saveCartToSession(HttpServletRequest request, PostCartDto cartDto);

    void removeCartFromSession(HttpServletRequest request);

    void incrementQuantity(Long itemId, HttpServletRequest request);

    void decrementQuantity(Long itemId, HttpServletRequest request);

    void removeItem(HttpServletRequest request, Long itemId);
}
