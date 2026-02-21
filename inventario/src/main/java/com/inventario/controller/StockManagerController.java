package com.inventario.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.enums.MovementType;
import com.inventario.model.StockManager;
import com.inventario.service.StockManagerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock manager", description = "Operations related with stock management")
public class StockManagerController {
    private final StockManagerService stockManagerService;

    public StockManagerController(StockManagerService stockManagerService) {
        this.stockManagerService = stockManagerService;
    }

    @Operation(summary = "Get transaction history", description = "Retrieves all stock records. If the 'type' parameter is provided, filters the results by that type of movement.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid Parameters")
    })
    @GetMapping
    public ResponseEntity<Page<StockManager>> getAllHistory(@RequestParam MovementType type, Pageable pageable) {
        if (type != null) {
            return ResponseEntity.ok(stockManagerService.getHistoryByType(type, pageable));
        }
        return ResponseEntity.ok(stockManagerService.getAllHistory(pageable));
    }

    @Operation(summary = "Get history by product ID", description = "Retrieves all stock movements associated with a specific product ID, sorted by descending creation date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History of the product success"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<StockManager>> getHistoryByProduct(@PathVariable Long productId, Pageable pageable) {
        return ResponseEntity.ok(stockManagerService.getHistoryByProduct(productId, pageable));
    }
}
