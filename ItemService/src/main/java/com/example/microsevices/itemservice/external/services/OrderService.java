package com.example.microsevices.itemservice.external.services;

import com.example.centralrepository.dtos.item.OrderItems;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderService {

    @GetMapping("/orders/items/{itemId}")
    List<OrderItems> getAllOrdersByItemId(@PathVariable Long itemId);

}
