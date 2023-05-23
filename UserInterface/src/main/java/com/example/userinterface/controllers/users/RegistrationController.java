package com.example.userinterface.controllers.users;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.dtos.users.RegisterDto;
import com.example.userinterface.external.services.ApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users/sign_up")
public class RegistrationController {

    private final ApiService apiService;

    public RegistrationController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/new")
    public String registrationForm(Model model){
        model.addAttribute("user", new RegisterDto());
        return "users/registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute RegisterDto registerDto, RedirectAttributes attributes, HttpServletRequest request) throws JsonProcessingException {
        HttpSession httpSession = request.getSession();
        GetUserDto userDto = (GetUserDto) httpSession.getAttribute("loggedInUser");
        if (userDto != null){
            attributes.addFlashAttribute("user", userDto);
            return "redirect:/";
        }
        try {
            userDto = apiService.registerUser(registerDto);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/users/sign_up/new";
        }
        httpSession.setAttribute("loggedInUser", userDto);
        attributes.addFlashAttribute("user", userDto);
        return "redirect:/";
    }
}
