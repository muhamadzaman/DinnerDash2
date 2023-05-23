package com.example.userinterface.external.dtos.orders;

import com.example.userinterface.external.dtos.users.GetUserDto;
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
    private GetUserDto user;
}
