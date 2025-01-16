package com.suga.kafkaconsumerservice.mapper;

import com.suga.kafkaconsumerservice.dto.ProductDto;
import com.suga.kafkaconsumerservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductDto entityToDto(Product product);
    @Mapping(target = "id", ignore = true)
    Product dtoToEntity(ProductDto productDto);
  }
