package com.example.nodotest.Mapper;

import com.example.nodotest.Dto.Request.CategoryRequest;
import com.example.nodotest.Dto.Request.ProductRequest;
import com.example.nodotest.Dto.Response.ProductResponse;
import com.example.nodotest.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "createdDate", expression = "java(toSqlDate(System.currentTimeMillis()))")
    @Mapping(target = "modifiedDate", expression = "java(toSqlDate(System.currentTimeMillis()))")
    @Mapping(target = "createdBy", constant = "admin")
    @Mapping(target = "modifiedBy", constant = "admin")
    @Mapping(target = "status", constant = "1")
    Product toEntity(ProductRequest productRequest);

    ProductResponse toResponse(Product product);

    List<Product> toEntities(List<ProductRequest> productRequest);

    List<ProductResponse> toResponses(List<Product> product);

    @Mapping(target = "id", ignore = true)
    void updateProductFromRequest(CategoryRequest req, @MappingTarget Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "createdDate", ignore = true) // Giữ nguyên createdDate
    @Mapping(target = "createdBy", ignore = true)   // Giữ nguyên createdBy
    @Mapping(target = "modifiedDate", expression = "java(toSqlDate(System.currentTimeMillis()))")
    @Mapping(target = "modifiedBy", constant = "admin")
    @Mapping(target = "status", ignore = true) // Giữ nguyên status
    void updateProductFromRequest(ProductRequest req, @MappingTarget Product product);

    default java.sql.Date toSqlDate(long timeInMillis) {
        return new java.sql.Date(timeInMillis);
    }
}
