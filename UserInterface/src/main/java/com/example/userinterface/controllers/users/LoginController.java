package com.example.userinterface.controllers.users;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.cart.GetCartDto;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.dtos.users.LoginDto;
import com.example.userinterface.external.services.ApiService;
import com.example.userinterface.external.services.CartSessionService;
import com.example.userinterface.external.services.impl.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Configuration
@RequestMapping("/users/sign_in")
@Slf4j
public class LoginController {

    private final ApiService apiService;

    private final AuthService authService;

    private final CartSessionService cartService;

    public LoginController(ApiService apiService, AuthService authService, CartSessionService cartService) {
        this.apiService = apiService;
        this.authService = authService;
        this.cartService = cartService;
    }

    @GetMapping("/new")
    public String loginForm(Model model){
        model.addAttribute("user", new LoginDto());
        return "users/login";
    }

    @PostMapping()
    public String login(@ModelAttribute LoginDto loginDto, HttpServletRequest request, RedirectAttributes attributes) throws JsonProcessingException {
        HttpSession httpSession = request.getSession();
        GetUserDto userDto = authService.getLoggedInUser(request);
        if (userDto != null) {
            attributes.addFlashAttribute("message", "Already logged in");
            return "redirect:/";
        }

        try {
            userDto = apiService.loginUser(loginDto);
        } catch (FeignException e) {
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/users/sign_in/new";
        }

        httpSession.setAttribute("loggedInUser", userDto);

        GetCartDto cartDto = apiService.getCartByUserId(userDto.getId());
        if (userDto.getRole().equals("ADMIN")) {
            cartService.removeCartFromSession(request);
        } else if (cartDto.getCartItems() == null || cartDto.getCartItems().size() == 0) {
            apiService.saveCartFromSession(userDto.getId(), cartService.getCartFromSession(httpSession));
            cartService.removeCartFromSession(request);
        } else {
            cartService.removeCartFromSession(request);
        }

        attributes.addFlashAttribute("messages", "Logged in successfully");
        return "redirect:/";
    }

}
