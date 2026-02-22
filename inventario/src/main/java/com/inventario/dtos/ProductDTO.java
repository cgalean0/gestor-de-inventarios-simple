package com.inventario.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.inventario.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockMin;
    private Integer amount;
    private String sku;
    private Category category;
    private LocalDateTime dateOfLastActualization;
}
