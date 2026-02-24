package com.inventario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.StackWalker.Option;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
import com.inventario.service.impl.ProductServiceImpl;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import com.inventario.service.StockManagerService;

@ExtendWith(MockitoExtension.class)
class InventarioApplicationTests {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductMapper productMapper;

	@Mock
	private StockManagerService stockManagerService;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	@DisplayName("Debe lanzar InsufficientStockException cuando el stock es menor a la salida")
	void shouldThrowExceptionWhenStockIsInsufficient() {
		Long id = 1L;
		Product mockProduct = new Product();
		mockProduct.setAmount(10);

		StockDTO stockDTO = new StockDTO();
		stockDTO.setAmount(50);
		stockDTO.setMotive("Venta excesiva");

		when(productRepository.findActiveById(id)).thenReturn(Optional.of(mockProduct));

		assertThrows(InsufficientStockException.class, () -> {
			productService.decreaseStock(id, stockDTO);
		});

		// Verificamos que nunca se llamó a la base de datos ni se registro en el
		// historial
		verify(productRepository, never()).save(any());
		verify(stockManagerService, never()).recordMovement(any(), any(), any(), any());
	}

	@Test
	@DisplayName("Debe restar la cantidad correcta y almacenar en el StockHistory")
	void stockCorrectOperation() {
		Long id = 1L;
		Product mockitoProduct = new Product();
		mockitoProduct.setAmount(100);
		mockitoProduct.setId(id);

		StockDTO stockDTO = new StockDTO();
		stockDTO.setAmount(50);
		stockDTO.setMotive("Venta xyz");

		when(productRepository.findActiveById(id)).thenReturn(Optional.of(mockitoProduct));
		when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

		productService.decreaseStock(id, stockDTO);
		assertEquals(50, mockitoProduct.getAmount());

		verify(productRepository, times(1)).save(mockitoProduct);

		verify(stockManagerService, times(1)).recordMovement(
				eq(mockitoProduct),
				eq(50),
				eq(MovementType.SALIDA),
				eq("Venta xyz"));
	}


    @Test
    @DisplayName("El nombre no debe ser vacío ni nulo")
    void shouldDetectInvalidName() {
        // GIVEN
        ProductCreationDTO dto = new ProductCreationDTO();
        dto.setName("");
        dto.setPrice(new BigDecimal("100"));
        dto.setStockMin(5);

        // WHEN
        var violations = validator.validate(dto);

        // THEN
        // Verificamos que existen violaciones de validación
        assertFalse(violations.isEmpty());
        
        // Verificar el mensaje de error específico
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

	// Validar que el SKU no sea repetido

	@Test
	@DisplayName("No debe existir dos SKU repetidos")
	void notRepeatSKU() {
		String duplicatedSKU = "AAA-0000";
		ProductCreationDTO mockitoProduct = new ProductCreationDTO();
		mockitoProduct.setSku(duplicatedSKU);
		mockitoProduct.setName("new Product");

		when(productRepository.existsBySkuAndDeletedFalse(duplicatedSKU)).thenReturn(true);
		assertThrows(DuplicateSkuException.class, () -> {
			productService.createProduct(mockitoProduct);
		});

		verify(productRepository, never()).save(any());
	}
	// Validar que el precio no sea negativo
	@Test
	@DisplayName("El precio no puede ser negativo")
	void priceCannotBeNegative() {
		ProductCreationDTO mockitoProd = new ProductCreationDTO();
		mockitoProd.setPrice(BigDecimal.valueOf(-569));

        // WHEN
        var violations = validator.validate(mockitoProd);

        // THEN
        // Verificamos que existen violaciones de validación
        assertFalse(violations.isEmpty());
        
        // Verificar el mensaje de error específico
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
	}
	// Validar que no se modifique productos que estan eliminados.

	@Test
	@DisplayName("No se pueden modificar productos eliminados")
	void cannotModifyDeletedProducts() {
		ProductUpdateDTO dto = new ProductUpdateDTO();
		dto.setName("New Prod2");
		dto.setPrice(BigDecimal.valueOf(1000));
		Long id = 1L;
		dto.setPrice(BigDecimal.valueOf(1000));
		Product mockitoProduct = new Product();
		mockitoProduct.setId(id);
		mockitoProduct.setName("New Prod");
		mockitoProduct.setPrice(BigDecimal.valueOf(1000));
		mockitoProduct.setDeleted(true);

		lenient().when(productRepository.findById(id)).thenReturn(Optional.of(mockitoProduct));

		assertThrows(ProductNotFoundException.class, () -> {
			productService.updateProduct(id, dto);
		});

	}
}
