package com.inventario.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inventario.enums.MovementType;
import com.inventario.model.Product;
import com.inventario.model.StockManager;
import com.inventario.repository.StockManagerRepository;
import com.inventario.service.StockManagerService;

@Service
public class StockManagerServiceImpl implements StockManagerService{

    private final StockManagerRepository stockManagerRepository;

    public StockManagerServiceImpl(StockManagerRepository stockManagerRepository) {
        this.stockManagerRepository = stockManagerRepository;
    }

    @Override
    public void recordMovement(Product product, Integer amount, MovementType movementType, String reason) {
        StockManager entry = new StockManager();
        entry.setProduct(product);
        entry.setAmount(amount);
        entry.setReason(reason);
        entry.setMovementType(movementType);

        stockManagerRepository.save(entry);
    }

    @Override
    public Page<StockManager> getAllHistory(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null.");
        }
        return stockManagerRepository.findAll(pageable);
    }

    @Override
    public Page<StockManager> getHistoryByProduct(Long productId, Pageable pageable) {
        if (productId == null) 
            throw new IllegalArgumentException("The productId cannot be null.");
        if (pageable == null)
            throw new IllegalArgumentException("Pageable cannot be null.");
        return stockManagerRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable);
    }

    @Override
    public Page<StockManager> getHistoryByType(MovementType type, Pageable pageable) {
        if (type == null) 
            throw new IllegalArgumentException("Type cannot be null.");
        if (pageable == null)
            throw new IllegalArgumentException("Pageable cannot be null.");
        return stockManagerRepository.getHistoryByMovementType(type, pageable);
    }

}
