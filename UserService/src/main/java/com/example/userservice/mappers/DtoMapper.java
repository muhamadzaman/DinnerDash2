package com.example.userservice.mappers;

import com.example.centralrepository.dtos.restaurant.GetRestaurantDto;
import com.example.centralrepository.dtos.restaurant.PostRestaurantDto;
import com.example.userservice.dto.GetUserDto;
import com.example.userservice.dto.RegisterDto;
import com.example.userservice.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GetUserDto covertToDto(User user, String token) {
        GetUserDto userDto = modelMapper.map(user, GetUserDto.class);
        userDto.setToken(token);
        return userDto;
    }

    public User convertDtoToUser(RegisterDto registerDto){
        return modelMapper.map(registerDto, User.class);
    }

    public RegisterDto convertGetToRegisterUserDto(GetUserDto userDto){
        return modelMapper.map(userDto, RegisterDto.class);
    }

    public User getUserFromUserDetails(UserDetails userDetails){
        return modelMapper.map(userDetails, User.class);
    }
}
