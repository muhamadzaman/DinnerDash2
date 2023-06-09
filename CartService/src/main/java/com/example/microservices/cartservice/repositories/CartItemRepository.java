package com.example.microservices.cartservice.repositories;

import com.example.microservices.cartservice.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCartId(String cartId);

}
