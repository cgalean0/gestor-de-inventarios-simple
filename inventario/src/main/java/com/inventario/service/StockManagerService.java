package com.inventario.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inventario.enums.MovementType;
import com.inventario.model.Product;
import com.inventario.model.StockManager;

public interface StockManagerService {
    void recordMovement(Product product, Integer amount, MovementType movementType, String reason);
    Page<StockManager> getAllHistory(Pageable pageable);
    Page<StockManager> getHistoryByProduct(Long productId, Pageable pageable);
    Page<StockManager> getHistoryByType(MovementType movementType, Pageable pageable);
}
