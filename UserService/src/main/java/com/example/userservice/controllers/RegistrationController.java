package com.example.userservice.controllers;

import com.example.userservice.dto.GetUserDto;
import com.example.userservice.dto.RegisterDto;
import com.example.userservice.entities.User;
import com.example.userservice.services.impl.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public GetUserDto register(@RequestBody RegisterDto registerDto){
        return userService.registerUser(registerDto);
    }

}
