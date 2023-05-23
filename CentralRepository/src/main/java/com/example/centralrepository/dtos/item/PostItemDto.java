package com.example.centralrepository.dtos.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostItemDto {

    private Long id;

    @NotBlank(message = "Title should not be blank!")
    private String title;

    @NotBlank(message = "Description should not be blank!")
    private String description;

    @Min(message = "Price must be greater than zero!", value = 1)
    private BigDecimal price;

    private Long restaurantId;

    @NotEmpty(message = "Atleast one category must exist")
    private List<Long> categoryIds;

    private MultipartFile file;
}
