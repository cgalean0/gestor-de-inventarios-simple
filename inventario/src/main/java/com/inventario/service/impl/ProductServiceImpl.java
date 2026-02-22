package com.inventario.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.inventario.dtos.AlertDTO;
import com.inventario.dtos.ProductCreationDTO;
import com.inventario.dtos.ProductDTO;
import com.inventario.dtos.ProductUpdateDTO;
import com.inventario.dtos.StockDTO;
import com.inventario.enums.MovementType;
import com.inventario.exceptions.DuplicateSkuException;
import com.inventario.exceptions.InsufficientStockException;
import com.inventario.exceptions.ProductNotFoundException;
import com.inventario.mappers.ProductMapper;
import com.inventario.model.Product;
import com.inventario.repository.ProductRepository;
import com.inventario.service.ProductService;
import com.inventario.service.StockManagerService;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final StockManagerService stockManagerService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
            StockManagerService stockManagerService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.stockManagerService = stockManagerService;
    }

    /**
     * ################
     * PRODUCTS MANAGMENT
     * ################
     */

    @Override
    @Transactional
    public ProductDTO createProduct(ProductCreationDTO request) {
        if (request == null)
            throw new IllegalArgumentException("The product is null");
        if (productRepository.existsBySkuAndDeletedFalse(request.getSku()))
            throw new DuplicateSkuException("The SKU: " + request.getSku() + " already exists.");
        Product product = productMapper.toEntity(request);
        if (product == null)
            throw new IllegalStateException("The product is null or dont exist.");
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Cacheable(value = "products", 
            key = "'' + #pageable.pageNumber + '-' + #pageable.pageSize + #pageable.sort.toString()",
            unless = "#result == null || #result.content.isEmpty()")
    public Page<ProductDTO> getProducts(Pageable pageable) {
        if (pageable == null)
            throw new IllegalArgumentException("Pageable cannot be null");
        return productRepository.findAllActive(pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Cacheable(value = "products", 
            key = "#id",
            unless = "#result == null")
    public ProductDTO getProductById(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id is null");
        Product foundProduct = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException("The product by ID: " + id + " not exists."));
        return productMapper.toDTO(foundProduct);
    }

    @Override
    @Cacheable(value = "products", 
            key = "#sku",
            unless = "#result == null")
    public ProductDTO getProductBySku(String sku) {
        if (sku == null)
            throw new IllegalArgumentException("sku is null");
        return productRepository.findOptionalBySkuAndDeletedFalse(sku)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "productLists", allEntries = true)
    })
    public ProductDTO updateProduct(Long id, ProductUpdateDTO product) {
        if (id == null)
            throw new IllegalArgumentException("The id is null.");
        Product findProduct = productRepository.findActiveById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (findProduct == null)
            throw new IllegalStateException("The product is null or dont exist.");
        productMapper.updateEntityFromDto(product, findProduct);
        return productMapper.toDTO(productRepository.save(findProduct));
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "productLists", allEntries = true)
    })
    public void deleteProduct(Long id) {
        if (id == null)
            throw new IllegalArgumentException("The id is null.");
        Product product = productRepository.findActiveById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.setDeleted(true);
        productRepository.saveAndFlush(product);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "productLists", allEntries = true)
    })
    public ProductDTO updateStock(Long id, Integer quantity, MovementType movementType, String reason) {
        Product product = productRepository.findActiveById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        if (movementType == MovementType.SALIDA) {
            if (product.getAmount() < quantity) {
                throw new InsufficientStockException("Insuficient stock.");
            }
            product.setAmount(product.getAmount() - quantity);
        } else { // Is ENTRADA or AJUSTE
            product.setAmount(product.getAmount() + quantity);
        }

        Product savedProduct = productRepository.save(product);
        stockManagerService.recordMovement(savedProduct, quantity, movementType, reason);
        return productMapper.toDTO(savedProduct);
    }

    /**
     * #################
     * STOCK MANAGMENT
     * #################
     */

    @Override
    @Transactional
    public ProductDTO increaseStock(Long id, StockDTO entrance) {
        validateStockDTO(entrance);
        return updateStock(id, entrance.getAmount(), MovementType.ENTRADA, entrance.getMotive());
    }

    @Override
    @Transactional
    public ProductDTO decreaseStock(Long id, StockDTO exit) {
        validateStockDTO(exit);
        return updateStock(id, exit.getAmount(), MovementType.SALIDA, exit.getMotive());
    }

    private void validateStockDTO(StockDTO dto) {
        if (dto.getAmount() <= 0 || dto == null)
            throw new IllegalArgumentException("The stock cannot be negative or zero.");
    }

    /**
     * ################
     * SEARCH & FILTERS
     * ################
     */

    @Override
    public Page<ProductDTO> searchByName(String name, Pageable pageable) {
        if (name.isBlank() || name == null)
            throw new IllegalArgumentException("The name cannot be Blank or Null.");
        return productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    public Page<ProductDTO> filterByRange(BigDecimal min, BigDecimal max, Pageable pageable) {
        if (min == null || max == null)
            throw new IllegalArgumentException("The values min or max are null.");

        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException("The min value cannot be major that max");

        return productRepository.findByPriceBetweenAndDeletedFalse(min, max, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    public List<AlertDTO> getLowStockAlerts() {
        return productRepository.findProductsWithLowStock();
    }

}
