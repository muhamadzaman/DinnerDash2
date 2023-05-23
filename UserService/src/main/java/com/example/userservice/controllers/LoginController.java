package com.example.userservice.controllers;

import com.example.userservice.dto.GetUserDto;
import com.example.userservice.dto.LoginDto;
import com.example.userservice.entities.User;
import com.example.userservice.mappers.DtoMapper;
import com.example.userservice.services.impl.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private final UserService userService;

    private final DtoMapper mapper;

    private final AuthenticationManager authenticationManager;

    public LoginController(UserService userService, DtoMapper mapper, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/sign_in", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GetUserDto> loginUser(LoginDto loginDto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = userService.loadUserByUsername(loginDto.getEmail());
        User user1 = mapper.getUserFromUserDetails(userDetails);
        return ResponseEntity.ok(mapper.covertToDto(user1, userService.generateToken(user1)));
    }

    @PostMapping("/sign_in/verify_token")
    public ResponseEntity<GetUserDto> verifyToken(@RequestHeader String token){
        return ResponseEntity.ok(userService.verifyToken(token));
    }
}
