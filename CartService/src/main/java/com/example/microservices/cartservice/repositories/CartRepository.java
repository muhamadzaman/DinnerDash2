package com.example.microservices.cartservice.repositories;

import com.example.microservices.cartservice.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUserId(Long userId);

}
