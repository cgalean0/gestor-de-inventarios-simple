package com.inventario.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.inventario.enums.Category;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
// Entablish Soft delete
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The name of product is required.")
    @Column(nullable = false)
    @Size(min = 3, max = 100, message = "The field name must be between 3 and 100 characters long.")
    private String name;

    @Size(max = 500)
    @Column(nullable = true, length = 500)
    private String description;

    @NotNull(message = "The amount of product is required.")
    @Column(nullable = false)
    @PositiveOrZero
    private Integer amount;

    @NotNull(message = "The stockMin of product is required.")
    @Column(name = "stock_min", nullable = false)
    @PositiveOrZero
    private Integer stockMin;

    @NotNull(message = "The price of product is required.")
    @Column(nullable = false, precision = 10, scale = 2)
    @Positive(message = "The price of the product must be positive.")
    private BigDecimal price;

    @NotNull(message = "The sku of product is required.")
    @Column(unique = true, nullable = false, length = 8)
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "SKU must match AAA-0000 format")
    private String sku;

    @CreatedDate
    @Column(name = "date_of_creation", nullable = false, updatable = false)
    private LocalDateTime dateOfCreation;
    
    @LastModifiedDate
    @Column(name = "last_actualization_date", insertable = false)
    private LocalDateTime dateOfLastActualization;

    // Used to enable the soft delete
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean deleted = false;

    @NotNull(message = "The category cannot be Null")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;
}
