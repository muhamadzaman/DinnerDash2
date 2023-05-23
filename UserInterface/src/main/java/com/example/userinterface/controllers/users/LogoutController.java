package com.example.userinterface.controllers.users;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.services.ApiService;
import com.example.userinterface.external.services.impl.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController {

    private final ApiService service;

    private final AuthService authService;

    public LogoutController(ApiService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @PostMapping("/users/logout")
    public String logoutUser(HttpServletRequest request, RedirectAttributes attributes) throws JsonProcessingException {
        GetUserDto userDto = authService.getLoggedInUser(request);
        if (userDto == null){
            attributes.addFlashAttribute("errors", "You are not logged in");
            return "redirect:/";
        }
        try {
            service.logout(userDto.getToken());
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        authService.logout(request);
        attributes.addFlashAttribute("messages", "Logged out Successfully");
        return "redirect:/";
    }
}
