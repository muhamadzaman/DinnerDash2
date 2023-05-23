package com.example.microservices.orderservice.dto;

import com.example.microservices.orderservice.entities.OrderItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderDto {

    private Long id;
    private String status;
    private LocalDateTime orderTime;
    private List<OrderItems> orderItems;
    private LocalDateTime statusTime;
    private Long userId;
}
