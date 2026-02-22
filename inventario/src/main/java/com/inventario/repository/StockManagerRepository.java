package com.inventario.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.enums.MovementType;
import com.inventario.model.StockManager;

public interface StockManagerRepository extends JpaRepository<StockManager, Long>{
    Page<StockManager> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);
    Page<StockManager> getHistoryByMovementType(MovementType movementType, Pageable pageable);
}
