package com.example.userinterface.controllers.cart;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.cart.GetCartDto;
import com.example.userinterface.external.dtos.cart.PostCartDto;
import com.example.userinterface.external.dtos.item.GetItemDto;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.services.ApiService;
import com.example.userinterface.external.services.impl.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static com.example.userinterface.controllers.cart.CartSessionController.getString;

@Controller
public class CartController {

    private final ApiService apiService;

    private final AuthService authService;

    public CartController(ApiService apiService, AuthService authService) {
        this.apiService = apiService;
        this.authService = authService;
    }

    @GetMapping("/users/{userId}/cart")
    public String getUserCart(@PathVariable Long userId, Model model, HttpServletRequest request, RedirectAttributes attributes){
        GetUserDto userDto = authService.getLoggedInUser(request);
        if (userDto == null || !Objects.equals(userDto.getId(), userId)){
            attributes.addFlashAttribute("errors", "You are not authorized");
            return "redirect:/";
        }
        GetCartDto cartDto;
        try{
            cartDto = apiService.getCartByUserId(userId);
        }catch (FeignException e){
            cartDto = null;
        }
        if (cartDto == null){
            GetCartDto getCartDto = apiService.saveCartFromSession(userId, new GetCartDto(UUID.randomUUID().toString(), userId, new ArrayList<>()));
            return getCartString(model, getCartDto);
        }
        cartDto.getCartItems().forEach(
                cartItem -> {
                    cartItem.setItem(apiService.getItemById(cartItem.getItemId()));
                }
        );
        return getCartString(model, cartDto);
    }

    @PutMapping("/cart/users/{userId}/items/{itemId}/increment")
    public String incrementQuantity(@PathVariable Long itemId, @PathVariable Long userId){
        apiService.incrementQuantity(userId, itemId);
        return "redirect:/users/" + userId + "/cart";
    }

    @PutMapping("/cart/users/{userId}/items/{itemId}/decrement")
    public String decrementQuantity(@PathVariable Long itemId, @PathVariable Long userId){
        apiService.decrementQuantity(userId, itemId);
        return "redirect:/users/" + userId + "/cart";
    }

    @DeleteMapping("/cart/users/{userId}/clear")
    public String deleteCart(@PathVariable Long userId){
        apiService.deleteCartByUserId(userId);
        return "redirect:/users/" + userId + "/cart";
    }

    @PostMapping("/cart/users/{userId}/save")
    public String saveCart(@PathVariable Long userId, @RequestParam("itemId") Long itemId, RedirectAttributes attributes) throws JsonProcessingException {
        GetCartDto cartDto = apiService.getCartByUserId(userId);
        GetItemDto itemDto;
        try {
            itemDto = apiService.getItemById(itemId);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        cartDto.getCartItems().forEach(
                cartItem -> {
                    cartItem.setItem(apiService.getItemById(cartItem.getItemId()));
                }
        );
        if (cartDto.getCartItems().size() > 0 && !Objects.equals(cartDto.getCartItems().get(0).getItem().getRestaurantId(), itemDto.getRestaurantId())){
            attributes.addFlashAttribute("errors", "You can add one restaurant item at a time in cart!");
            return "redirect:/";
        }
        PostCartDto postCartDto = new PostCartDto(itemId, 1);
        apiService.saveCartItems(userId, postCartDto);
        return "redirect:/users/" + userId + "/cart";
    }

    @DeleteMapping("/cart/users/{userId}/items/{itemId}/remove")
    public String removeCartItem(@PathVariable Long userId, @PathVariable Long itemId){
        apiService.removeCartItem(userId, itemId);
        return "redirect:/users/" + userId + "/cart";
    }

    private String getCartString(Model model, GetCartDto cartDto) {
        return getString(model, cartDto);
    }
}
