package com.example.userinterface.external.dtos.orders;

import com.example.userinterface.external.dtos.item.GetItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {
    private Long id;
    private Long itemId;
    private Long orderId;
    private Integer quantity;
    private GetItemDto itemDto;
}
