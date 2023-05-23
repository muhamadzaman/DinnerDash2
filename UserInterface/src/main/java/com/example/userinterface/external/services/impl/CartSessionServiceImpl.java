package com.example.userinterface.external.services.impl;

import com.example.userinterface.external.dtos.cart.CartItem;
import com.example.userinterface.external.dtos.cart.GetCartDto;
import com.example.userinterface.external.dtos.cart.PostCartDto;
import com.example.userinterface.external.services.ApiService;
import com.example.userinterface.external.services.CartSessionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartSessionServiceImpl implements CartSessionService {

    private final ApiService apiService;

    public CartSessionServiceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void removeCartFromSession(HttpServletRequest request) {
        request.getSession().removeAttribute("session_cart");
    }

    @Override
    public GetCartDto saveCartToSession(HttpServletRequest request, PostCartDto cartDto) {
        HttpSession session = request.getSession();
        List<CartItem> cartItems = getSessionCart(session);

        CartItem cartItem = cartItems.stream().filter(
                item -> item.getItemId().equals(cartDto.getItemId())
        ).findFirst().orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setItemId(cartDto.getItemId());
            newItem.setQuantity(0);
            cartItems.add(newItem);
            return newItem;
        });
        cartItem.setQuantity(cartItem.getQuantity() + cartDto.getQuantity());
        session.setAttribute("session_cart", cartItems);
        return new GetCartDto(session.getId(), null, cartItems);
    }

    @Override
    public GetCartDto getCartFromSession(HttpSession httpSession){
        List<CartItem> cartItems = getSessionCart(httpSession);
        cartItems.forEach(
                cartItem -> {
                    cartItem.setItem(apiService.getItemById(cartItem.getItemId()));
                }
        );
        return new GetCartDto(httpSession.getId(), null, cartItems);
    }

    @Override
    public void incrementQuantity(Long itemId, HttpServletRequest request) {
        PostCartDto cartDto = new PostCartDto(itemId, 1);
        saveCartToSession(request, cartDto);
    }

    @Override
    public void decrementQuantity(Long itemId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<CartItem> cartItems = getSessionCart(session);
        CartItem cartItem = cartItems.stream().filter(
                item -> item.getItemId().equals(itemId)
        ).findFirst().orElse(null);
        if (cartItem != null){
            if (cartItem.getQuantity() > 1){
                cartItem.setQuantity(cartItem.getQuantity() - 1);
            } else {
                cartItems.remove(cartItem);
            }
            session.setAttribute("session_cart", cartItems);
        }
    }

    @Override
    public void removeItem(HttpServletRequest request, Long itemId) {
        HttpSession session = request.getSession();
        List<CartItem> cartItems = getSessionCart(session);
        CartItem cartItem = cartItems.stream().filter(
                item -> item.getItemId().equals(itemId)
        ).findFirst().orElse(null);
        if (cartItem != null){
            cartItems.remove(cartItem);
            session.setAttribute("session_cart", cartItems);
        }
    }

    private List<CartItem> getSessionCart(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("session_cart");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }
}
