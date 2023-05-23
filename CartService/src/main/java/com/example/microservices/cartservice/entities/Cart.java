package com.example.microservices.cartservice.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    private String id;

    private Long userId;
}
