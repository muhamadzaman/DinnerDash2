package com.example.microservices.orderservice.mappers;

import com.example.microservices.orderservice.dto.GetOrderDto;
import com.example.microservices.orderservice.entities.Order;
import com.example.microservices.orderservice.entities.OrderItems;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoMapper {

    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GetOrderDto convertOrderToDto(Order order) {
        return modelMapper.map(order, GetOrderDto.class);
    }
}
