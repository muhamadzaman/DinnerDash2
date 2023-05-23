package com.example.microservices.orderservice.controllers;

import com.example.microservices.orderservice.OrderServiceApplication;
import com.example.microservices.orderservice.dto.GetOrderDto;
import com.example.microservices.orderservice.dto.PostOrderDto;
import com.example.microservices.orderservice.entities.OrderItems;
import com.example.microservices.orderservice.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<GetOrderDto>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<GetOrderDto>> getAllOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getAllOrdersByUserId(userId));
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<GetOrderDto> createOrder(@PathVariable Long userId, @RequestBody PostOrderDto orderDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto ,userId));
    }

    @PutMapping("/{orderId}/paid")
    public ResponseEntity<GetOrderDto> updateOrderStatusToPaid(@PathVariable Long orderId){
        return  ResponseEntity.ok(orderService.updateOrderStatusToPaid(orderId));
    }

    @PutMapping("/{orderId}/completed")
    public ResponseEntity<GetOrderDto> updateOrderStatusToCompleted(@PathVariable Long orderId){
        return  ResponseEntity.ok(orderService.updateOrderStatusToCompleted(orderId));
    }

    @PutMapping("/{orderId}/cancelled")
    public ResponseEntity<GetOrderDto> updateOrderStatusToCancelled(@PathVariable Long orderId){
        return  ResponseEntity.ok(orderService.updateOrderStatusToCancelled(orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderDto> getOrderByOrderId(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOneOrder(orderId));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<List<OrderItems>> getAllOrdersByItemId(@PathVariable Long itemId){
        return ResponseEntity.ok(orderService.getOrderItemsByItemId(itemId));
    }
}
