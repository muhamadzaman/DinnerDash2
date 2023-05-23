package com.example.microservices.restaurantservice.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantsConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
