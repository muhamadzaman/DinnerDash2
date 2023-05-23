package com.example.microservices.cartservice.controllers;

import com.example.centralrepository.dtos.cart.PostCartDto;
import com.example.microservices.cartservice.dtos.GetCartDto;
import com.example.microservices.cartservice.services.CartService;
import com.example.microservices.cartservice.services.CartSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class CartController {

    private final CartService cartService;

//    private final CartSessionService cartSessionService;

//    private final HttpServletRequest request;

    private final HttpSession session;

    public CartController(CartService cartService, HttpSession session) {
        this.cartService = cartService;
        this.session = session;
    }

//    @GetMapping("/session/cart")
//    public ResponseEntity<GetCartDto> getCartFromSession(){
//        return ResponseEntity.ok(cartSessionService.getCartFromSession(session));
//    }
//
//    @PostMapping("/session/cart")
//    public ResponseEntity<GetCartDto> saveCartToSession(@RequestBody @Valid PostCartDto cartDto){
//        return ResponseEntity.status(HttpStatus.CREATED).body(cartSessionService.saveCartToSession(request, cartDto));
//    }
//
//    @DeleteMapping("/session/cart/clear")
//    public void clearCart(){
//        cartSessionService.removeCartFromSession(request);
//    }

//    @PostMapping("/cart/users/{userId}")
//    public ResponseEntity<GetCartDto> saveCartFromSession(@PathVariable Long userId){
//        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.saveCartFromSession(session, userId));
//    }

    @PostMapping("/cart/users/{userId}")
    public ResponseEntity<GetCartDto> saveCartByUserId(@PathVariable Long userId, @RequestBody GetCartDto cartDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.saveCartFromSession(userId, cartDto));
    }

    @GetMapping("/cart/users/{userId}")
    public ResponseEntity<GetCartDto> getCartByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/cart/users/{userId}")
    public void deleteCartByUserId(@PathVariable Long userId){
        cartService.clearUserCart(userId);
    }

    @PostMapping("/cart/users/{userId}/items/{itemId}/increment")
    public ResponseEntity<GetCartDto> incrementQuantity(@PathVariable Long userId, @PathVariable Long itemId){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.incrementQuantity(itemId, userId));
    }

    @PostMapping("/cart/users/{userId}/items/{itemId}/decrement")
    public ResponseEntity<GetCartDto> decrementQuantity(@PathVariable Long userId, @PathVariable Long itemId){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.decrementQuantity(itemId, userId));
    }

    @PostMapping("/cart/users/{userId}/save")
    public ResponseEntity<GetCartDto> saveCartItems(@PathVariable Long userId, @RequestBody PostCartDto cartDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.saveCartItemByUserId(userId, cartDto));
    }

    @DeleteMapping("/cart/users/{userId}/items/{itemId}/remove")
    public ResponseEntity<GetCartDto> removeCartItem(@PathVariable Long userId, @PathVariable Long itemId){
        return ResponseEntity.ok(cartService.removeCartItem(userId, itemId));
    }
}
