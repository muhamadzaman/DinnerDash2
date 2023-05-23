package com.example.userinterface.controllers;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.cart.GetCartDto;
import com.example.userinterface.external.dtos.orders.GetOrderDto;
import com.example.userinterface.external.dtos.orders.OrderItems;
import com.example.userinterface.external.dtos.orders.PostOrderDto;
import com.example.userinterface.external.services.ApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    private final ApiService apiService;

    public OrderController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/orders/users/{userId}/new")
    public String createOrder(@PathVariable Long userId, Model model){
        GetCartDto cartDto = apiService.getCartByUserId(userId);
        PostOrderDto postOrderDto = new PostOrderDto();
        postOrderDto.setOrderItems(
                cartDto.getCartItems().stream().map(
                        cartItem -> {
                            OrderItems orderItems = new OrderItems();
                            orderItems.setItemId(cartItem.getItemId());
                            orderItems.setQuantity(cartItem.getQuantity());
                            return orderItems;
                        }
                ).toList()
        );
        GetOrderDto orderDto = apiService.createOrder(userId, postOrderDto);
        apiService.deleteCartByUserId(userId);
        return "redirect:/orders/" + orderDto.getId();
    }

    @GetMapping("/orders/{orderId}")
    public String show(@PathVariable Long orderId, Model model, RedirectAttributes attributes) throws JsonProcessingException {
        GetOrderDto orderDto;
        try{
            orderDto = apiService.getOrderByOrderId(orderId);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        orderDto.setUser(apiService.getUserById(orderDto.getUserId()));
        orderDto.getOrderItems().forEach(
                orderItems -> orderItems.setItemDto(apiService.getItemById(orderItems.getItemId()))
        );
        List<BigDecimal> prices = new ArrayList<>();
        orderDto.getOrderItems().forEach(
                orderItem -> prices.add(orderItem.getItemDto().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
        );
        model.addAttribute("order", orderDto);
        model.addAttribute("price", prices);
        return "orders/show";
    }

    @GetMapping("/orders")
    public String index(
            Model model,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "status", required = false) String status){
        if (userId != null){
            List<GetOrderDto> itemDtos = apiService.getAllOrdersByUserId(userId);
            model.addAttribute("orders", itemDtos);
            return "orders/index";
        }
        List<GetOrderDto> orderDtos = apiService.getAllOrders();
        if (status != null){
            List<GetOrderDto> orders = orderDtos.stream().filter(
                    getOrderDto -> getOrderDto.getStatus().equals(status)
            ).toList();
            model.addAttribute("size", orderDtos.size());
            model.addAttribute("orders", orders);
            return "orders/index";
        }
        model.addAttribute("size", orderDtos.size());
        model.addAttribute("orders", orderDtos);
        return "orders/index";
    }

    @PutMapping("/orders/{orderId}/paid")
    public String updateStatusToPaid(@PathVariable Long orderId, RedirectAttributes attributes) throws JsonProcessingException {
        try {
            apiService.updateOrderStatusToPaid(orderId);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        return "redirect:/orders";
    }

    @PutMapping("/orders/{orderId}/completed")
    public String updateStatusToCompleted(@PathVariable Long orderId, RedirectAttributes attributes) throws JsonProcessingException {
        try {
            apiService.updateOrderStatusToCompleted(orderId);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        return "redirect:/orders";
    }

    @PutMapping("/orders/{orderId}/cancelled")
    public String updateStatusToCancelled(@PathVariable Long orderId, RedirectAttributes attributes) throws JsonProcessingException {
        try {
            apiService.updateOrderStatusToCancelled(orderId);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        return "redirect:/orders";
    }
}
