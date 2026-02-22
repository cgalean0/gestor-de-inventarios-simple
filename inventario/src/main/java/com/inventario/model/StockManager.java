package com.inventario.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.inventario.enums.MovementType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock_manager")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StockManager{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;
    
    @NotNull(message = "The amount cannot be null")
    @Column(nullable = false, updatable = false)
    private Integer amount;
    
    @NotNull(message = "The StockType cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type")
    private MovementType movementType;

    @NotNull(message = "The StockType cannot be null")
    @NotBlank(message = "The StockType cannot be Blank")
    @Column(nullable = false, updatable = false, length = 255)
    private String reason;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
