package com.example.userinterface.external.dtos.item;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostItemDto {

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private Long restaurantId;

    private List<Long> categoryIds;

    private MultipartFile file;
}
