package com.example.microservices.cartservice.services.impl;

import com.example.centralrepository.dtos.cart.PostCartDto;
import com.example.microservices.cartservice.dtos.GetCartDto;
import com.example.microservices.cartservice.entities.Cart;
import com.example.microservices.cartservice.entities.CartItem;
import com.example.microservices.cartservice.mappers.DtoMapper;
import com.example.microservices.cartservice.services.CartSessionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartSessionServiceImpl implements CartSessionService {

    private final DtoMapper dtoMapper;

    public CartSessionServiceImpl(DtoMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
    }

    @Override
    public void removeCartFromSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    @Override
    public GetCartDto saveCartToSession(HttpServletRequest request, PostCartDto cartDto) {
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("session_cart");
        if (cartItems == null){
            cartItems = new ArrayList<>();
        }
        System.out.println(session.getId());
        Optional<CartItem> cartItemOptional = cartItems.stream().filter(
                cartItem -> cartItem.getItemId().equals(cartDto.getItemId())
        ).findFirst();
        if (cartItemOptional.isPresent()){
            cartItemOptional.get().setQuantity(cartItemOptional.get().getQuantity() + cartDto.getQuantity());
            session.setAttribute("session_cart", cartItems);
            Cart cart = new Cart(session.getId(), null);
            return dtoMapper.mapToGetDto(cart, cartItems);
        }
        CartItem cartItem = new CartItem();
        cartItem.setCartId(session.getId());
        cartItem.setItemId(cartDto.getItemId());
        cartItem.setQuantity(cartDto.getQuantity());
        cartItems.add(cartItem);
        session.setAttribute("session_cart", cartItems);
        Cart cart = new Cart(session.getId(), null);
        return dtoMapper.mapToGetDto(cart, cartItems);
    }

    @Override
    public GetCartDto getCartFromSession(HttpSession httpSession){
        @SuppressWarnings("unchecked")
        List<CartItem> cartItems = (List<CartItem>) httpSession.getAttribute("session_cart");
        if (cartItems == null){
            cartItems = new ArrayList<>();
        }
        Cart cart = new Cart(httpSession.getId(), null);
        return dtoMapper.mapToGetDto(cart, cartItems);
    }
}
