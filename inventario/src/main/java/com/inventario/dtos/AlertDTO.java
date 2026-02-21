package com.inventario.dtos;

public record AlertDTO(
        Long id,
        String name,
        String sku,
        Integer amount,
        Integer stockMin,
        Integer deficit) {
}