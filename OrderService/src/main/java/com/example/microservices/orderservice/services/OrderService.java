package com.example.microservices.orderservice.services;

import com.example.microservices.orderservice.dto.GetOrderDto;
import com.example.microservices.orderservice.dto.PostOrderDto;
import com.example.microservices.orderservice.entities.OrderItems;

import java.util.List;

public interface OrderService {

    List<GetOrderDto> getAllOrders();

    List<GetOrderDto> getAllOrdersByUserId(Long userId);

    GetOrderDto createOrder(PostOrderDto order, Long userId);

    GetOrderDto getOneOrder(Long orderId);

    GetOrderDto updateOrderStatusToPaid(Long orderId);

    GetOrderDto updateOrderStatusToCompleted(Long orderId);

    GetOrderDto updateOrderStatusToCancelled(Long orderId);

    List<OrderItems> getOrderItemsByItemId(Long itemId);
}
