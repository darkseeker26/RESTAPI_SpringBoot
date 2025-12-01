package com.codewithmosh.store.mappers;


import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.web.bind.annotation.RequestMapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto request);

    @Mapping(target="id",ignore=true )
    void update(ProductDto request, @MappingTarget Product product);
}
