package com.example.userinterface.controllers;

import com.example.userinterface.external.dtos.cart.CartItem;
import com.example.userinterface.external.dtos.cart.GetCartDto;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.services.ApiService;
import com.example.userinterface.external.services.impl.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckoutController {

    private final AuthService authService;

    private final ApiService apiService;

    public CheckoutController(AuthService authService, ApiService apiService) {
        this.authService = authService;
        this.apiService = apiService;
    }

    @GetMapping("/checkout")
    public String checkout(HttpServletRequest request, RedirectAttributes attributes, Model model) {
        GetUserDto userDto = authService.getLoggedInUser(request);
        if (userDto == null){
            attributes.addFlashAttribute("errors", "You need to be signed in to checkout");
            return "redirect:/users/sign_in/new";
        } else if (userDto.getRole().equals("ADMIN")) {
            attributes.addFlashAttribute("errors", "Admin cannot checkout");
            return "redirect:/";
        }
        GetCartDto cartDto = apiService.getCartByUserId(userDto.getId());
        cartDto.getCartItems().forEach(
                cartItem -> {
                    cartItem.setItem(apiService.getItemById(cartItem.getItemId()));
                }
        );
        Optional<CartItem> cartItems = cartDto.getCartItems().stream().filter(cartItem -> cartItem.getItem().getRetired()).findFirst();
        if (cartItems.isPresent()) {
            attributes.addFlashAttribute("errors", "You can't place order of a retired item");
            return "redirect:/users/" + userDto.getId() + "/cart";
        }
        List<BigDecimal> prices = new ArrayList<>();
        cartDto.getCartItems().forEach(
                cartItem -> prices.add(cartItem.getItem().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
        );
        model.addAttribute("cart", cartDto);
        model.addAttribute("price", prices);
        return "orders/checkout";
    }
}
