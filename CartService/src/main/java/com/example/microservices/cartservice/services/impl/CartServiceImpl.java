package com.example.microservices.cartservice.services.impl;

import com.example.centralrepository.dtos.cart.PostCartDto;
import com.example.microservices.cartservice.dtos.GetCartDto;
import com.example.microservices.cartservice.entities.Cart;
import com.example.microservices.cartservice.entities.CartItem;
import com.example.microservices.cartservice.mappers.DtoMapper;
import com.example.microservices.cartservice.repositories.CartItemRepository;
import com.example.microservices.cartservice.repositories.CartRepository;
import com.example.microservices.cartservice.services.CartService;
import com.example.microservices.cartservice.services.CartSessionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

//    private final ModelMapper modelMapper;
    private final DtoMapper dtoMapper;

    private final CartItemRepository cartItemRepository;

//    private final CartSessionService cartSessionService;

    public CartServiceImpl(CartRepository cartRepository, DtoMapper dtoMapper, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.dtoMapper = dtoMapper;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public GetCartDto getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                () -> new NoSuchElementException("Unable to find Cart with user id: " + userId)
        );
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        return dtoMapper.mapToGetDto(cart, cartItems);
    }

//    @Override
//    public GetCartDto saveCartFromSession(HttpSession httpSession, Long userId) {
//        GetCartDto getCartDto = cartSessionService.getCartFromSession(httpSession);
//        Cart cart = new Cart(getCartDto.getId(), userId);
//        Cart savedCart = cartRepository.save(cart);
//        List<CartItem> cartItems = getCartDto.getCartItems();
//        List<CartItem> savedCartItems = cartItemRepository.saveAll(cartItems);
//        return dtoMapper.mapToGetDto(savedCart, savedCartItems);
//    }

    @Override
    public void clearUserCart(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty()) {
            throw new NoSuchElementException("No cart found against user id: " + userId);
        }
        List<CartItem> allByCartId = cartItemRepository.findAllByCartId(cart.get().getId());
        cartItemRepository.deleteAll(allByCartId);
        cartRepository.deleteById(cart.get().getId());
    }

    @Override
    public GetCartDto saveCartFromSession(Long userId, GetCartDto cartDto) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isPresent()) {
            List<CartItem> cartItems = cartDto.getCartItems();
            cartItems.forEach(item -> item.setCartId(optionalCart.get().getId()));
            List<CartItem> savedCartItems = cartItemRepository.saveAll(cartItems);
            return dtoMapper.mapToGetDto(optionalCart.get(), savedCartItems);
        }
        Cart savedCart = cartRepository.save(new Cart(cartDto.getId(), userId));
        List<CartItem> cartItems = cartDto.getCartItems();
        List<CartItem> savedCartItems = cartItemRepository.saveAll(cartItems);
        cartItems.forEach(item -> item.setCartId(savedCart.getId()));
        return dtoMapper.mapToGetDto(savedCart, savedCartItems);
    }

    @Override
    public GetCartDto incrementQuantity(Long itemId, Long userId) {
        return getCartDto(itemId, userId, 1);
    }

    @Override
    public GetCartDto decrementQuantity(Long itemId, Long userId) {
        return getCartDto(itemId, userId, -1);
    }

    private GetCartDto getCartDto(Long itemId, Long userId, Integer quantity){
        Optional<Cart> cartDto = cartRepository.findByUserId(userId);
        if (cartDto.isPresent()) {
            List<CartItem> cartItems = cartItemRepository.findAllByCartId(cartDto.get().getId());
            cartItems.forEach(
                    cartItem -> {
                        if (Objects.equals(cartItem.getItemId(), itemId)) {
                            if (cartItem.getQuantity() > 1 && quantity < 0) {
                                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                                cartItemRepository.save(cartItem);
                            } else if (cartItem.getQuantity() == 1 && quantity < 0) {
                                cartItemRepository.delete(cartItem);
                            } else if (cartItem.getQuantity() >= 1 && quantity > 0) {
                                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                                cartItemRepository.save(cartItem);
                            }
                        }
                    }
            );
            return dtoMapper.mapToGetDto(cartDto.get(), cartItems);
        }
        else
            throw new NoSuchElementException("No cart with user id " + userId + " found");
    }

    @Override
    public GetCartDto saveCartItemByUserId(Long userId, PostCartDto postCartDto) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isPresent()){
            List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.get().getId());
            Optional<CartItem> optionalCartItem = cartItems.stream().filter(
                    cartItem -> cartItem.getItemId().equals(postCartDto.getItemId())
            ).findFirst();
            if (optionalCartItem.isPresent()) {
                optionalCartItem.get().setQuantity(optionalCartItem.get().getQuantity() + postCartDto.getQuantity());
                cartItemRepository.save(optionalCartItem.get());
            }else{
                CartItem cartItem = new CartItem(null, cart.get().getId(), postCartDto.getItemId(), postCartDto.getQuantity());
                cartItemRepository.save(cartItem);
            }
            return dtoMapper.mapToGetDto(cart.get(), cartItemRepository.findAllByCartId(cart.get().getId()));
        }
        else {
            Cart savedCart = cartRepository.save(new Cart(UUID.randomUUID().toString(), userId));
            cartItemRepository.save(new CartItem(
                    null,
                    savedCart.getId(),
                    postCartDto.getItemId(),
                    postCartDto.getQuantity()));
            List<CartItem> cartItems = cartItemRepository.findAllByCartId(savedCart.getId());
            return dtoMapper.mapToGetDto(savedCart, cartItems);
        }
    }

    @Override
    public GetCartDto removeCartItem(Long userId, Long itemId) {
        GetCartDto getCartDto = getCartByUserId(userId);
        getCartDto.getCartItems().forEach(
                cartItem -> {
                    if (Objects.equals(cartItem.getItemId(), itemId)) {
                        cartItemRepository.delete(cartItem);
                    }
                }
        );
        return getCartDto;
    }
}
