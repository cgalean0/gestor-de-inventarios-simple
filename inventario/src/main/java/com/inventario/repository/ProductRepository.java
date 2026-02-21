package com.inventario.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventario.dtos.AlertDTO;
import com.inventario.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySkuAndDeletedFalse(String sku);
    Optional<Product> findByIdAndDeletedFalse(Long id);
    
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deleted = false")
    Optional<Product> findActiveById(Long id);

    Optional<Product> findOptionalBySkuAndDeletedFalse(String sku);

    Page<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);

    Page<Product> findByPriceBetweenAndDeletedFalse(BigDecimal min, BigDecimal max, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deleted = false")
    Page<Product> findAllActive(Pageable pageable);

    @Query("SELECT new com.inventario.dtos.AlertDTO(p.id, p.name, p.sku, p.amount, p.stockMin, (p.stockMin - p.amount)) " +
       "FROM Product p " +
       "WHERE p.amount < p.stockMin AND p.deleted = false " +
       "ORDER BY (p.stockMin - p.amount) DESC")
    List<AlertDTO> findProductsWithLowStock();
}
