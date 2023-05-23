package com.example.microsevices.itemservice.entities;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    private String title;
    private String description;
    @Column(precision = 8, scale = 2)
    private BigDecimal price;
    private Long restaurantId;
    private Boolean retired = false;

    @Column(name = "image_url")
    private String imageUrl;
}
