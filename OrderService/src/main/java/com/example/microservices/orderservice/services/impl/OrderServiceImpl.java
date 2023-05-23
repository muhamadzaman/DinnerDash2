package com.example.microservices.orderservice.services.impl;

import com.example.microservices.orderservice.dto.GetOrderDto;
import com.example.microservices.orderservice.dto.PostOrderDto;
import com.example.microservices.orderservice.entities.Order;
import com.example.microservices.orderservice.entities.OrderItems;
import com.example.microservices.orderservice.entities.Status;
import com.example.microservices.orderservice.mappers.DtoMapper;
import com.example.microservices.orderservice.repositories.OrderItemsRepository;
import com.example.microservices.orderservice.repositories.OrderRepository;
import com.example.microservices.orderservice.services.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemsRepository orderItemsRepository;

    private final DtoMapper dtoMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemsRepository orderItemsRepository, DtoMapper dtoMapper) {
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public List<GetOrderDto> getAllOrders() {
        List<GetOrderDto> orderDtos = orderRepository.findAll().stream().map(dtoMapper::convertOrderToDto).toList();
        orderDtos.forEach(this::setOrderItems);
        return orderDtos;
    }

    @Override
    public List<GetOrderDto> getAllOrdersByUserId(Long userId) {
        List<GetOrderDto> orderDtos = orderRepository
                .findAllByUserId(userId)
                .stream()
                .map(dtoMapper::convertOrderToDto)
                .toList();
        orderDtos.forEach(this::setOrderItems);
        return orderDtos;
    }

    @Override
    public GetOrderDto createOrder(PostOrderDto orderDto, Long userId) {
        Order order = new Order(null, Status.ordered, userId, LocalDateTime.now(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        List<OrderItems> orderItems = orderDto.getOrderItems().stream().map(
                orderItems1 -> {
                    OrderItems orderItem = new OrderItems();
                    orderItem.setItemId(orderItems1.getItemId());
                    orderItem.setOrderId(savedOrder.getId());
                    orderItem.setQuantity(orderItems1.getQuantity());
                    return orderItemsRepository.save(orderItem);
                }
        ).toList();
        GetOrderDto getOrderDto = dtoMapper.convertOrderToDto(savedOrder);
        getOrderDto.setOrderItems(orderItems);
        return getOrderDto;
    }

    @Override
    public GetOrderDto getOneOrder(Long orderId) {
        GetOrderDto orderDto = orderRepository.findById(orderId).map(dtoMapper::convertOrderToDto).orElseThrow(
                () -> new NoSuchElementException("No order exits with id " + orderId)
        );
        return setOrderItems(orderDto);
    }

    @Override
    public GetOrderDto updateOrderStatusToPaid(Long orderId) {
        return setStatus(Status.paid, orderId);
    }

    @Override
    public GetOrderDto updateOrderStatusToCompleted(Long orderId) {
        return setStatus(Status.completed, orderId);
    }

    @Override
    public GetOrderDto updateOrderStatusToCancelled(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchElementException("No order exits with id " + orderId)
        );
        if (order.getStatus() == Status.ordered || order.getStatus() == Status.paid){
            order.setStatus(Status.cancelled);
            order.setStatusTime(LocalDateTime.now());
            GetOrderDto getOrderDto = dtoMapper.convertOrderToDto(orderRepository.save(order));
            return setOrderItems(getOrderDto);
        }
        GetOrderDto getOrderDto = dtoMapper.convertOrderToDto(order);
        return setOrderItems(getOrderDto);
    }

    private GetOrderDto setStatus(Status status, Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchElementException("No order exits with id " + orderId)
        );
        order.setStatus(status);
        order.setStatusTime(LocalDateTime.now());
        GetOrderDto getOrderDto = dtoMapper.convertOrderToDto(orderRepository.save(order));
        return setOrderItems(getOrderDto);
    }

    private GetOrderDto setOrderItems(GetOrderDto orderDto){
        orderDto.setOrderItems(
                orderItemsRepository.findByOrderId(orderDto.getId())
        );
        return orderDto;
    }

    @Override
    public List<OrderItems> getOrderItemsByItemId(Long itemId) {
        return orderItemsRepository.findAllByItemId(itemId);
    }
}
