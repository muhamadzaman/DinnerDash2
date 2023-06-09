package com.example.microservices.orderservice.repositories;

import com.example.microservices.orderservice.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    List<OrderItems> findByOrderId(Long orderId);

    List<OrderItems> findAllByItemId(Long itemId);
}
