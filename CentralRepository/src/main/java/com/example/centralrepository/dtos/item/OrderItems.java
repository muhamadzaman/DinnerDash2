package com.example.centralrepository.dtos.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {

    private Long id;
    private Long itemId;
    private Long orderId;
    private Integer quantity;

}
