package com.example.microservices.restaurantservice.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue
    @Column(name = "restaurant_id")
    private Long id;
    private String name;
    private  String address;

}
