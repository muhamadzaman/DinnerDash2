package com.example.userinterface.controllers.cart;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.cart.GetCartDto;
import com.example.userinterface.external.dtos.cart.PostCartDto;
import com.example.userinterface.external.dtos.item.GetItemDto;
import com.example.userinterface.external.services.ApiService;
import com.example.userinterface.external.services.CartSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class CartSessionController {

    private final CartSessionService cartSessionService;

    private final ApiService apiService;

    public CartSessionController(CartSessionService cartSessionService, ApiService apiService) {
        this.cartSessionService = cartSessionService;
        this.apiService = apiService;
    }

    @GetMapping("/session/cart")
    public String getCartFromSession(HttpSession session, Model model){
        GetCartDto cartDto = cartSessionService.getCartFromSession(session);
        return getCartString(model, cartDto);
    }

    @PostMapping("/session/cart")
    public String saveCartToSession(
            HttpServletRequest request,
            @RequestParam("itemId") Long itemId,
            PostCartDto postCartDto,
            RedirectAttributes attributes) throws JsonProcessingException {
        postCartDto.setItemId(itemId);
        GetItemDto itemDto;
        try {
            itemDto = apiService.getItemById(itemId);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        GetCartDto getCartDto = cartSessionService.getCartFromSession(request.getSession());
        if (getCartDto.getCartItems().size() > 0 && !Objects.equals(getCartDto.getCartItems().get(0).getItem().getRestaurantId(), itemDto.getRestaurantId())){
            attributes.addFlashAttribute("errors", "You can add one restaurant item at a time in cart!");
            return "redirect:/";
        }
        cartSessionService.saveCartToSession(request, postCartDto);
        return "redirect:/session/cart";
    }

    @PutMapping("/session/cart/items/{itemId}/increment")
    public String incrementQuantity(HttpServletRequest request, @PathVariable Long itemId){
        cartSessionService.incrementQuantity(itemId, request);
        return "redirect:/session/cart";
    }

    @PutMapping("/session/cart/items/{itemId}/decrement")
    public String decrementQuantity(HttpServletRequest request, @PathVariable Long itemId){
        cartSessionService.decrementQuantity(itemId, request);
        return "redirect:/session/cart";
    }

    @DeleteMapping("/session/cart/items/{itemId}/remove")
    public String removeItem(HttpServletRequest request, @PathVariable Long itemId){
        cartSessionService.removeItem(request, itemId);
        return "redirect:/session/cart";
    }

    @DeleteMapping("/session/cart/clear")
    public String clearCart(HttpServletRequest request){
        cartSessionService.removeCartFromSession(request);
        return "redirect:/session/cart";
    }

    private String getCartString(Model model, GetCartDto cartDto) {
        return getString(model, cartDto);
    }

    static String getString(Model model, GetCartDto cartDto) {
        List<BigDecimal> prices = new ArrayList<>();
        cartDto.getCartItems().forEach(
                cartItem -> {
                    prices.add(cartItem.getItem().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                }
        );
        model.addAttribute("cart", cartDto);
        model.addAttribute("price", prices);
        return "cart/show";
    }
}