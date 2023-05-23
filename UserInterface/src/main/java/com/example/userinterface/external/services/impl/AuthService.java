package com.example.userinterface.external.services.impl;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.services.ApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class AuthService {

    private final ApiService apiService;

    public AuthService(ApiService apiService) {
        this.apiService = apiService;
    }

    public GetUserDto getLoggedInUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        return (GetUserDto) session.getAttribute("loggedInUser");
    }

    public Boolean verifyUser(String token) throws JsonProcessingException {
        GetUserDto userDto;
        try {
            userDto = apiService.verifyUser(token);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            log.info(apiError.getErrors().toString());
            return false;
        }
        return userDto != null && userDto.getToken().equals(token);
    }

    public void logout(HttpServletRequest request){
        request.getSession().removeAttribute("loggedInUser");
    }
}
