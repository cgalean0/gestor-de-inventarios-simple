package com.inventario.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.dtos.AlertDTO;
import com.inventario.dtos.ProductCreationDTO;
import com.inventario.dtos.ProductDTO;
import com.inventario.dtos.ProductUpdateDTO;
import com.inventario.dtos.StockDTO;
import com.inventario.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operations related with product management")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * #################
     * PRODUCTS ENDPOINTS
     * #################
     */
    @Operation(summary = "Get product by ID", description = "Return a singles product based on its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Create a new product", description = "Register a new product in the system. The SKU must be unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Product with given SKU already exists")
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreationDTO productCreationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productCreationDTO));
    }

    @Operation(summary = "Get all products paginated", description = "Retrieves a page of products. Parameters: page (0-N), size, sort.")
    @ApiResponse(responseCode = "200", description = "List of products retrieved")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getProducts(pageable));
    }

    @Operation(summary = "Get product by SKU", description = "Retrieve a product using its unique SKU code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }

    @Operation(summary = "Update an existing product", description = "Update editable fields (name, description, price, stockMin). SKU and amount cannot be modified here.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
            @RequestBody ProductUpdateDTO productUpdateDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productUpdateDTO));
    }

    @Operation(summary = "Delete a product", description = "Permanently remove a product from the database by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * #################
     * STOCK ENDPOINTS
     * #################
     */

    @Operation(summary = "Increase product stock", description = "Add a specific quantity to the current stock of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid amount provided"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{id}/stock/increase")
    public ResponseEntity<ProductDTO> increaseStock(@PathVariable Long id, @RequestBody StockDTO entrance) {
        return ResponseEntity.ok(productService.increaseStock(id, entrance));
    }

    @Operation(summary = "Decrease product stock", description = "Subtract a specific quantity from the current stock of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock decreased successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock or invalid amount"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{id}/stock/decrease")
    public ResponseEntity<ProductDTO> decreaseStock(@PathVariable Long id, @RequestBody StockDTO exit) {
        return ResponseEntity.ok(productService.decreaseStock(id, exit));
    }

    /**
     * #################
     * SEARCH & FILTER ENDPOINTS
     * #################
     */
    @Operation(summary = "Search products by name", description = "Filter products by a partial name match (case-insensitive)")
    @ApiResponse(responseCode = "200", description = "List of matching products")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> getProductByName(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(productService.searchByName(name, pageable));
    }

    @Operation(summary = "Filter products by price range", description = "Retrieve products within a minimum and maximum price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products within the range"),
            @ApiResponse(responseCode = "400", description = "Invalid range (e.g., min > max)")
    })
    @GetMapping("/search/price")
    public ResponseEntity<Page<ProductDTO>> getProductByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Pageable pageable) {
        return ResponseEntity.ok(productService.filterByRange(minPrice, maxPrice, pageable));
    }

    /**
     * #################
     * STOCK ALERTS ENDPOINTS
     * #################
     */
    @Operation(summary = "Get low stock alerts", description = "Retrieves a list of products where the current quantity is below the minimum stock level.")
    @ApiResponse(responseCode = "200", description = "List of products with stock deficit retrieved successfully")
    @GetMapping("/alerts")
    public ResponseEntity<List<AlertDTO>> getAlerts() {
        return ResponseEntity.ok(productService.getLowStockAlerts());
    }
}
