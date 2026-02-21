package com.inventario.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.inventario.dtos.ProductCreationDTO;
import com.inventario.dtos.ProductDTO;
import com.inventario.dtos.ProductUpdateDTO;
import com.inventario.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfLastActualization", ignore = true)
    @Mapping(target = "dateOfCreation", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Product toEntity(ProductCreationDTO dto);

    @Mapping (target = "id", ignore = true)
    @Mapping (target = "amount", ignore = true)
    @Mapping (target = "sku", ignore = true)
    @Mapping (target = "dateOfLastActualization", ignore = true)
    @Mapping(target = "dateOfCreation", ignore = true)
    @Mapping(target = "deleted", ignore = true)    
    void updateEntityFromDto(ProductUpdateDTO dto, @MappingTarget Product entity);
}
