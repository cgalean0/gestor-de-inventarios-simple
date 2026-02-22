package com.inventario.dtos;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StockDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    @NotNull(message = "The amount cannot be Null")
    @Positive(message = "The amount must be postive")
    private Integer amount;
    @NotNull(message = "The motive cannot be Null")
    @NotBlank(message = "The motive cannot be blank")
    private String motive;
}
