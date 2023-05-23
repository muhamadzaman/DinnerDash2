package com.example.userinterface.external.dtos.item;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetItemDto implements Serializable {

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private Long restaurantId;

    private Boolean retired;

    private List<Long> categoryIds;

    private String imageUrl;
}
