package com.inventario.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

import com.inventario.enums.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Name cannot be blank.")
    @NotNull(message = "The name of product is required.")
    @Schema(example = "Coca-cola", description = "The name is required")
    private String name;

    private String description;

    @NotNull(message = "The price of product is required.")
    @Positive(message = "The price of product must be positive.")
    @Schema(example = "5000", description = "The price must be Positive")
    private BigDecimal price;

    @NotNull(message = "The stockMin of product is required.")
    @PositiveOrZero(message = "The stockMin of product must be positive or zero.")
    @Schema(example = "7", description = "The stock min must be positive or zero")
    private Integer stockMin;

    @NotBlank(message = "Category cannot be blank.")
    @NotNull(message = "The Category of product is required.")
    @Schema(example = "FRUTAS_Y_VERDURAS", description = "The Category of these products")
    private Category category;
}
