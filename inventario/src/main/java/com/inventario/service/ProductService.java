package com.inventario.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.inventario.dtos.AlertDTO;
import com.inventario.dtos.ProductCreationDTO;
import com.inventario.dtos.ProductDTO;
import com.inventario.dtos.ProductUpdateDTO;
import com.inventario.dtos.StockDTO;
import com.inventario.enums.MovementType;

public interface ProductService {
    ProductDTO createProduct(ProductCreationDTO product);
    Page<ProductDTO> getProducts(Pageable pageable);
    ProductDTO getProductById(Long id);
    ProductDTO getProductBySku(String sku);
    ProductDTO updateProduct(Long id, ProductUpdateDTO product);
    void deleteProduct(Long id);
    void updateStock(Long id, Integer quantity, MovementType type, String reason)

    // Stock managment
    ProductDTO increaseStock(Long id, StockDTO entrance);
    ProductDTO decreaseStock(Long id, StockDTO exit);

    // Searching and filters
    Page<ProductDTO> searchByName(String name, Pageable pageable);

    Page<ProductDTO> filterByRange(BigDecimal min, BigDecimal max, Pageable pageable);

    // Alerts of stocks
    List<AlertDTO> getLowStockAlerts();
}
