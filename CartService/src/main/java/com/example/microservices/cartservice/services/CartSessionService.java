package com.example.microservices.cartservice.services;

import com.example.centralrepository.dtos.cart.PostCartDto;
import com.example.microservices.cartservice.dtos.GetCartDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface CartSessionService {

    GetCartDto getCartFromSession(HttpSession httpSession);

    GetCartDto saveCartToSession(HttpServletRequest request, PostCartDto cartDto);

    void removeCartFromSession(HttpServletRequest request);

}
